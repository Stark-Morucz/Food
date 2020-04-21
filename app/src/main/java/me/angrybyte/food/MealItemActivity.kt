package me.angrybyte.food

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_meal_item.*
import me.angrybyte.food.DTS.MealItem

class MealItemActivity : AppCompatActivity() {

    lateinit var dbHandler : DBHandler
    var categoryId : Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_meal_item)
        supportActionBar?.title = intent.getStringExtra(INTENT_CATEGORY_NAME)
        categoryId = intent.getLongExtra(INTENT_CATEGORY_ID, -1)
        dbHandler = DBHandler(this)
        rv_meal_item.layoutManager = LinearLayoutManager(this)


        fab_meal_item.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this)
            dialog.setTitle("Adding new meal")
            val view = layoutInflater.inflate(R.layout.dialog_meal_item, null)
            val foodItem = view.findViewById<TextInputEditText>(R.id.tv_mealItem)
            dialog.setView(view)
            dialog.setPositiveButton("OK") { _: DialogInterface, _: Int ->
                if (foodItem.text.toString().isNotEmpty()) {
                    val item = MealItem()
                    item.name = foodItem.text.toString()
                    item.MealItemId = categoryId
                    item.isCompleted = false
                    dbHandler.addMealItem(item)
                    refreshing()
                }
            }
            dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->

            }
            dialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshing()
    }

    fun updateMealItemName(item : MealItem){
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("Editing meal")
        val view = layoutInflater.inflate(R.layout.dialog_meal_item, null)
        val foodItem = view.findViewById<TextInputEditText>(R.id.tv_mealItem)
        foodItem.setText(item.name)
        dialog.setView(view)
        dialog.setPositiveButton("OK") { _: DialogInterface, _: Int ->
            if (foodItem.text.toString().isNotEmpty()) {
                item.name = foodItem.text.toString()
                item.MealItemId = categoryId
                item.isCompleted = false
                dbHandler.updateMealItem(item)
                refreshing()
            }
        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
        dialog.show()
    }

    private fun refreshing() {
        rv_meal_item.adapter = MealItemAdapter(this, dbHandler.getMealItems(categoryId))
    }

    class MealItemAdapter(val activity: MealItemActivity, val list: MutableList<MealItem>) : RecyclerView.Adapter<MealItemAdapter.ViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_meal_item, parent, false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.mealItemName.text = list[position].name
            holder.mealItemName.isChecked = list[position].isCompleted
            holder.mealItemName.setOnClickListener{
                list[position].isCompleted = !list[position].isCompleted
                activity.dbHandler.updateMealItem(list[position])
            }
            holder.deleteBtn.setOnClickListener{
                val dialog = MaterialAlertDialogBuilder(activity)
                dialog.setTitle("Deleting meal item")
                dialog.setMessage("Are you sure, you want to delete this meal?")
                dialog.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                    activity.dbHandler.deleteMealItem(list[position].id)
                    activity.refreshing()
                }
                dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
                dialog.show()
            }

            holder.editBtn.setOnClickListener{
                activity.updateMealItemName(list[position])
                activity.refreshing()
            }
        }

        class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
            val mealItemName : MaterialCheckBox = v.findViewById(R.id.cb_meal_item)
            val deleteBtn : ImageView = v.findViewById(R.id.iv_delete)
            val editBtn : ImageView = v.findViewById(R.id.iv_edit)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}