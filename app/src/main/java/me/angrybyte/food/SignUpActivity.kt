package me.angrybyte.food

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        validateForm()

        tv_username_signup.addTextChangedListener(inputChangeListener)
        tv_password_signup.addTextChangedListener(inputChangeListener)
        tv_passwordConfirm_signup.addTextChangedListener(inputChangeListener)

        btn_sign_up_signup.setOnClickListener(){
            signUpUser()
        }
    }

    private fun validateForm() {

        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(tv_username_signup.text.toString()).matches()
        val  passNotValid = tv_password_signup.text.toString().isEmpty()
        val isPassConfValid =  tv_passwordConfirm_signup.text.toString().equals(tv_password_signup.text.toString()) && tv_passwordConfirm_signup.text.toString().isNotEmpty()

        email_TIL_signup.isErrorEnabled = !isEmailValid
        password_TIL_signup.isErrorEnabled = passNotValid
        passwordConfirm_TIL_signup.isErrorEnabled = !isPassConfValid

                if (!isEmailValid) {
                    email_TIL_signup.error = "Enter valid email formula!"
                }

                if (passNotValid) {
                    password_TIL_signup.error = "Enter a password!"
                }

                if(!isPassConfValid) {
                    passwordConfirm_TIL_signup.error = "Passwords mismatch!"
                }

        btn_sign_up_signup.isEnabled = isEmailValid && !passNotValid && isPassConfValid
    }


    private fun signUpUser() {
            auth.createUserWithEmailAndPassword(
                tv_username_signup.text.toString(),
                tv_password_signup.text.toString()
            ).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                       // val auth = FirebaseAuth.getInstance()
                        val user = auth.currentUser

                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                }
                            }
                    } else {
                        Toast.makeText(
                            baseContext, "Process failed!. Try again!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

