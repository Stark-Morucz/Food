package me.angrybyte.food

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        btn_sign_up_signin.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        btn_log_in_signin.setOnClickListener{
            doLogin()
        }

    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI (currentUser: FirebaseUser?){
        if(currentUser != null){
            if(currentUser.isEmailVerified) {
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(baseContext, "Email is not verified yet.",
                    Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(baseContext, "Error. Wrong username or password.",
                Toast.LENGTH_SHORT).show()
        }
    }

    fun doLogin() {
        if(tv_username_signin.text.toString().isEmpty()){
            email_TIL_signin.error = "Enter your email!"
            tv_username_signin.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(tv_username_signin.text.toString()).matches()) {
            email_TIL_signin.error = "Enter valid email formula!"
            tv_username_signin.requestFocus()
            return
        }

        if(tv_password_signin.text.toString().isEmpty()) {
            password_TIL_signin.error = "Enter password!"
            tv_password_signin.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(tv_username_signin.text.toString(), tv_password_signin.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }
            }

    }
}
