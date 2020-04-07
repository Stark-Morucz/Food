package me.angrybyte.food.DTS

class Category() {

    var id : Long = -1
    var name = ""
    var createdAt = ""
    var items : MutableList<MealItem> = ArrayList()
}