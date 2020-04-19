package me.angrybyte.food

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        auth = FirebaseAuth.getInstance()
        btn_own_ideas.isEnabled = auth.currentUser != null

        btn_own_ideas.setOnClickListener {
            startActivity(Intent(this, CookBookActivity::class.java))
        }

       ///TODO make sing out button and fuction, make button only available if the user previuosly logged in, and then start main activity, here is the function for logging out: FirebaseAuth.getInstance().signOut();

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
