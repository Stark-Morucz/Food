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
import kotlinx.android.synthetic.main.activity_breakfast.*
import me.starkmorucz.food.R

class BreakfastActivity : AppCompatActivity() {

    val meals: ArrayList<String> = ArrayList()
    val details: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breakfast)
        rv_breakfast.layoutManager = LinearLayoutManager(this)
        rv_breakfast.adapter =
            BreakfastAdapter(
                meals,
                details,
                this
            )
        addBreakfast()
    }

    fun addBreakfast(){
        meals.add("spinach omelette")
        details.add("Carbohydrate: 0.4g,\nProtein: 6.7g,\nCaloria: 78 kcal,\nFat: 5g")
        meals.add("Apple and cinnamon porridge with cottage cheese")
        details.add("Carbohydrate: 4.8g,\nProtein: 10.4g,\nCaloria: 82 kcal,\nFat: 2.5g")
        meals.add("Chia pudding with kiwi")
        details.add("Carbohydrate: 11g,\nProtein: 2g,\nCaloria: 74 kcal,\nFat: 3.1g")
        meals.add("Zucchini chips with cheese")
        details.add("Carbohydrate: 62g,\nProtein: 13g,\nCaloria: 389 kcal,\nFat: 8.3g")
        meals.add("Chives with cottage cheese cream sandwich")
        details.add("Carbohydrate: 15g,\nProtein: 12g,\nCaloria: 170 kcal,\nFat: 45g")
        meals.add("Soft boiled eggs, ham, pepper")
        details.add("Carbohydrate: 15g,\nProtein: 12g,\nCaloria: 170 kcal,\nFat: 45g")
        meals.add("Chia pudding with almond milk")
        details.add("Carbohydrate: 15g,\nProtein: 12g,\nCaloria: 170 kcal,\nFat: 45g")
    }

    class BreakfastAdapter(val items: ArrayList<String>, val details: ArrayList<String>, val context: Context) : RecyclerView.Adapter<BreakfastAdapter.ViewHolder>() {

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
            holder.breakfastName.text = items[position]
            holder.breakfastDetails.text = details[position]
        }

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val breakfastName : TextView = v.findViewById(R.id.tv_meal_name)
            val breakfastDetails : TextView = v.findViewById(R.id.tv_meal_details)
        }
    }
}
