package me.starkmorucz.food.role.user

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
import me.starkmorucz.food.DataClasses.MealItem
import me.starkmorucz.food.R
import me.starkmorucz.food.database.DBHandler
import me.starkmorucz.food.database.INTENT_CATEGORY_ID
import me.starkmorucz.food.database.INTENT_CATEGORY_NAME

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
            val cal = view.findViewById<TextInputEditText>(R.id.tv_calory)
            val fat = view.findViewById<TextInputEditText>(R.id.tv_fat)
            val carb = view.findViewById<TextInputEditText>(R.id.tv_carb)
            val protein = view.findViewById<TextInputEditText>(R.id.tv_protein)
            dialog.setView(view)
            dialog.setPositiveButton("OK") { _: DialogInterface, _: Int ->
                if (foodItem.text.toString().isNotEmpty()) {

                    val item = MealItem()
                    item.name = foodItem.text.toString()
                    item.MealItemId = categoryId
                    item.isCompleted = false

                        item.Cal = cal.text.toString().toLong()

                        item.Protein = protein.text.toString().toLong()

                        item.Carb = carb.text.toString().toLong()

                        item.Fat = fat.text.toString().toLong()

                        dbHandler.addMealItem(item)
                        refreshing()
                    }
            }
            dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
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

        val cal = view.findViewById<TextInputEditText>(R.id.tv_calory)
        cal.setText(item.Cal.toString())
        val fat = view.findViewById<TextInputEditText>(R.id.tv_fat)
        fat.setText(item.Fat.toString())
        val carb = view.findViewById<TextInputEditText>(R.id.tv_carb)
        carb.setText(item.Carb.toString())
        val protein = view.findViewById<TextInputEditText>(R.id.tv_protein)
        protein.setText(item.Protein.toString())

        dialog.setView(view)
        dialog.setPositiveButton("OK") { _: DialogInterface, _: Int ->
            if (foodItem.text.toString().isNotEmpty()) {
                item.name = foodItem.text.toString()
                item.MealItemId = categoryId
                item.isCompleted = false

                if(cal.text.toString().isNotEmpty())
                    item.Cal = cal.text.toString().toLong()
                else
                    item.Cal = 0

                if(carb.text.toString().isNotEmpty())
                    item.Carb = carb.text.toString().toLong()
                else
                    item.Carb = 0

                if(fat.text.toString().isNotEmpty())
                    item.Fat = fat.text.toString().toLong()
                else
                    item.Fat = 0

                if(protein.text.toString().isNotEmpty())
                    item.Protein = protein.text.toString().toLong()
                else
                    item.Protein = 0

                dbHandler.updateMealItem(item)
                refreshing()
            }
        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
        dialog.show()
    }

    private fun refreshing() {
        rv_meal_item.adapter =
            MealItemAdapter(
                this,
                dbHandler.getMealItems(categoryId)
            )
    }

    class MealItemAdapter(val activity: MealItemActivity, val list: MutableList<MealItem>) : RecyclerView.Adapter<MealItemAdapter.ViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(activity).inflate(
                    R.layout.recycler_meal_item,
                    parent,
                    false
                )
            )
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

            holder.mealItemName.setOnLongClickListener {
                val dialog = MaterialAlertDialogBuilder(activity)
                dialog.setTitle(list[position].name)
                dialog.setMessage(
                        "Calory: " + list[position].Cal + " kcal\n" +
                        "Protein: " + list[position].Protein +  " g\n" +
                        "Carbohydrate: " + list[position].Carb + " g\n" +
                        "Fat: " + list[position].Fat + " g"
                )
                dialog.setPositiveButton("OK") { _: DialogInterface, _: Int -> }
                dialog.show()
                true
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