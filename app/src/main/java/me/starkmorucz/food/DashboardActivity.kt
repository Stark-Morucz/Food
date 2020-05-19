package me.starkmorucz.food

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*
import me.starkmorucz.food.role.guest.BreakfastActivity
import me.starkmorucz.food.role.guest.DinnerActivity
import me.starkmorucz.food.role.guest.LunchActivity
import me.starkmorucz.food.role.user.CookBookActivity

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
            dialog.setTitle(R.string.sign_out)
            dialog.setMessage(R.string.sign_out_confirm)
            dialog.setPositiveButton(R.string.alert_ok) { _: DialogInterface, _: Int ->
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            dialog.setNegativeButton(R.string.alert_cancel) { _: DialogInterface, _: Int -> }
            dialog.show()
        }

        btn_own_ideas.setOnClickListener {
            startActivity(Intent(this, CookBookActivity::class.java))
        }

        btn_breakfast.setOnClickListener {
            startActivity(Intent(this, BreakfastActivity::class.java))
         }

        btn_lunch.setOnClickListener {
            startActivity(Intent(this, LunchActivity::class.java))
        }

        btn_dinner.setOnClickListener {
            startActivity(Intent(this, DinnerActivity::class.java))
        }
    }



}
