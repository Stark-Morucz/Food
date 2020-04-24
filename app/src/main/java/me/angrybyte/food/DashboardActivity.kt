package me.angrybyte.food

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        auth = FirebaseAuth.getInstance()
        btn_own_ideas.isEnabled = auth.currentUser != null

        if(auth.currentUser != null){
            fab_signOut.show()
        } else {
            fab_signOut.hide()
        }

        fab_signOut.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this)
            dialog.setTitle("Sign Out")
            dialog.setMessage("Are you sure you want to sing out?")
            dialog.setPositiveButton("OK") { _: DialogInterface, _: Int ->
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
            dialog.show()
        }

        btn_own_ideas.setOnClickListener {
            startActivity(Intent(this, CookBookActivity::class.java))
        }

        btn_breakfast.setOnClickListener {
            startActivity(Intent(this, MealItemActivity::class.java))  ///TODO breakfast activity
         }

        btn_lunch.setOnClickListener {
           // startActivity(Intent(this, MainActivity::class.java))  ///TODO lunch activity
        }

        btn_dinner.setOnClickListener {
           // startActivity(Intent(this, MainActivity::class.java))  ///TODO dinner activity
        }
    }



}
