package me.angrybyte.food

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cook_book2.*
import me.angrybyte.food.DTS.Category

class CookBookActivity : AppCompatActivity() {

    lateinit var dbHandler: DBHandler
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cook_book2)
        auth = FirebaseAuth.getInstance()
        dbHandler = DBHandler(this)
        rv_cookbook.layoutManager = LinearLayoutManager(this)

        fab_cook_book.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this)
            dialog.setTitle("Adding new category")
            val view = layoutInflater.inflate(R.layout.dialog_cook_book, null)
            val categoryName = view.findViewById<TextInputEditText>(R.id.tv_cookbook)
            dialog.setView(view)
            dialog.setPositiveButton("OK") { _: DialogInterface, _: Int ->
                if (categoryName.text.toString().isNotEmpty()) {
                    val category = Category()
                    category.name = categoryName.text.toString()
                    category.owner = auth.currentUser?.getUid().toString()
                    dbHandler.addCategory(category)
                    refreshing()
                }
            }
            dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
            dialog.show()
        }
    }

    override fun onResume(){
        refreshing()
        super.onResume()
    }

    fun updateCategoryName(category: Category){
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("Updating category")
        val view = layoutInflater.inflate(R.layout.dialog_cook_book, null)
        val categoryName = view.findViewById<TextInputEditText>(R.id.tv_cookbook)
        categoryName.setText(category.name)
        dialog.setView(view)
        dialog.setPositiveButton("OK") { _: DialogInterface, _: Int ->
            if (categoryName.text.toString().isNotEmpty()) {
                category.name = categoryName.text.toString()
                dbHandler.updateCategory(category)
                refreshing()
            }
        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
        dialog.show()
    }

    private fun refreshing(){
        rv_cookbook.adapter = CookBookAdapter(this, dbHandler.getCategories(auth.currentUser?.getUid().toString()))
    }

    class CookBookAdapter(val activity: CookBookActivity, val list: MutableList<Category>) : RecyclerView.Adapter<CookBookAdapter.ViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_cook_book, parent, false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.cookBookName.text = list[position].name

            holder.cookBookCard.setOnClickListener{
                val intent = Intent(activity, MealItemActivity::class.java)
                intent.putExtra(INTENT_CATEGORY_ID, list[position].id)
                intent.putExtra(INTENT_CATEGORY_NAME, list[position].name)
                activity.startActivity(intent)
            }

            holder.menu.setOnClickListener{
                val popup = PopupMenu(activity, holder.menu)
                popup.inflate(R.menu.cookbook_child)
                popup.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_edit->{
                            activity.updateCategoryName(list[position])
                        }
                        R.id.menu_delete->{
                            activity.dbHandler.deleteCategory(list[position].id)
                            activity.refreshing()
                        }
                        R.id.menu_complete->{
                            activity.dbHandler.markComplete(list[position].id)
                        }
                    }
                    true
                }
                popup.show()
            }
        }

        class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
            val cookBookName : TextView = v.findViewById(R.id.tv_cook_book_name)
            val cookBookCard : CardView = v.findViewById(R.id.card_cook_book)
            val menu: ImageView = v.findViewById(R.id.iv_menu)
        }
    }
}