package me.starkmorucz.food.role.guest

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_dinner.*
import me.starkmorucz.food.R

class DinnerActivity : AppCompatActivity() {

    val meals: ArrayList<String> = ArrayList()
    val details: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dinner)
        rv_dinner.layoutManager = LinearLayoutManager(this)
        rv_dinner.adapter =
            DinnerAdapter(
                meals,
                details,
                this
            )
        addDinner()
    }

    fun addDinner(){
        meals.add("Spinach omelette dinner")
        details.add("Carbohydrate: 0.4g,\nProtein: 6.7g,\nCaloria: 78 kcal,\nFat: 5g")
        meals.add("Apple and cinnamon porridge with cottage cheese")
        details.add("Carbohydrate: 4.8g,\nProtein: 10.4g,\nCaloria: 82 kcal,\nFat: 2.5g")
        meals.add("Chia pudding with kiwi")
        details.add("Carbohydrate: 11g,\nProtein: 2g,\nCaloria: 74 kcal,\nFat: 3.1g")
        meals.add("Zucchini chips with cheese")
        details.add("Carbohydrate: 62g,\nProtein: 13g,\nCaloria: 389 kcal,\nFat: 8.3g")
    }

    class DinnerAdapter(val items: ArrayList<String>, val details: ArrayList<String>, val context: Context) : RecyclerView.Adapter<DinnerAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.recycler_fix_meals,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.dinnerName.text = items[position]
            holder.dinnerDetails.text = details[position]
        }

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val dinnerName : TextView = v.findViewById(R.id.tv_meal_name)
            val dinnerDetails : TextView = v.findViewById(R.id.tv_meal_details)
        }
    }
}
