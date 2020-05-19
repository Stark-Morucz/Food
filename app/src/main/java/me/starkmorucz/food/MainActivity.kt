package me.starkmorucz.food

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val inputChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            validateForm()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        tv_username_signin.addTextChangedListener(inputChangeListener)
        tv_password_signin.addTextChangedListener(inputChangeListener)

        btn_sign_up_signin.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        btn_log_in_signin.setOnClickListener{
            doLogin()
        }

        btn_login_without_profile.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
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
                Toast.makeText(baseContext, R.string.error_email_confirm.toString(),
                    Toast.LENGTH_SHORT).show()
            }
        } else {
            if(!tv_username_signin.text.toString().isEmpty())
                Toast.makeText(baseContext, R.string.error_credentials.toString(),
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateForm() : Boolean {

        val isUserNameValid = tv_username_signin.text.toString().isNotEmpty()
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(tv_username_signin.text.toString()).matches()
        val isPassValid =  tv_password_signin.text.toString().isNotEmpty()

        email_TIL_signin.isErrorEnabled = !isUserNameValid || !isEmailValid
        password_TIL_signin.isErrorEnabled = !isPassValid

        if (!isUserNameValid) {
            email_TIL_signin.error = R.string.error_email.toString()
        }

        if (!isEmailValid) {
            email_TIL_signin.error = R.string.error_email_formula.toString()
        }

        if(!isPassValid) {
            password_TIL_signin.error = R.string.error_password.toString()
        }

        btn_log_in_signin.isEnabled = isEmailValid && isUserNameValid && isPassValid
        return isEmailValid && isUserNameValid && isPassValid
    }

    fun doLogin() {
        if(!validateForm()) return

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