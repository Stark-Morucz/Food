package me.angrybyte.food

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cook_book2.*
import kotlinx.android.synthetic.main.dialog_cook_book.*
import kotlinx.android.synthetic.main.dialog_cook_book.view.*
import me.angrybyte.food.DTS.Category

class CookBookActivity : AppCompatActivity() {

    lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cook_book2)
        fab_cook_book.backgroundTintList =
            ColorStateList.valueOf(5025616)  //this is colorPrimary in integer
        dbHandler = DBHandler(this)
        fab_cook_book.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_cook_book, null)
            val categoryName = view.tv_cookbook.text.toString()
            dialog.setPositiveButton("OK") { _: DialogInterface, _: Int ->
                if (categoryName.isEmpty()) {
                    cook_book_TIL.error = "Enter name"
                } else {
                    val category = Category()
                    category.name = categoryName
                    dbHandler.addCategory(category)
                }
            }
            dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->

            }
            dialog.show()
        }
    }
}
