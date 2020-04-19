package me.angrybyte.food

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.firebase.auth.FirebaseAuth
import me.angrybyte.food.DTS.Category
import me.angrybyte.food.DTS.MealItem


class DBHandler(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(db: SQLiteDatabase) {
        auth = FirebaseAuth.getInstance()

        val createCategoryTable =
            "CREATE TABLE $TABLE_CATEGORY (" +
                "$COL_ID integer PRIMARY KEY AUTOINCREMENT," +
                "$COL_CREATED_AT datetime DEFAULT CURRENT_TIMESTAMP," +
                "$COL_NAME varchar," +
                "$COL_MADEBY varchar2);"
        val createMealItemTable =
            "CREATE TABLE $TABLE_MEAL_ITEM (" +
                    "$COL_ID integer PRIMARY KEY AUTOINCREMENT," +
                    "$COL_CREATED_AT datetime DEFAULT CURRENT_TIMESTAMP," +
                    "$COL_MEAL_ITEM_ID integer," +
                    "$COL_MEAL_ITEM_NAME varchar," +
                    "$COL_IS_COMPLETED integer);"

        db.execSQL(createCategoryTable)
        db.execSQL(createMealItemTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun markComplete(mealItemId: Long) {
        val db = readableDatabase
        val queryResult = db.rawQuery("SELECT * FROM $TABLE_MEAL_ITEM WHERE $COL_MEAL_ITEM_ID=$mealItemId", null)

        if (queryResult.moveToFirst()) {
            do {
                val item = MealItem()
                item.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                item.MealItemId = queryResult.getLong(queryResult.getColumnIndex(COL_MEAL_ITEM_ID))
                item.name = queryResult.getString(queryResult.getColumnIndex(COL_MEAL_ITEM_NAME))
                item.isCompleted = true
                updateMealItem(item)
            } while (queryResult.moveToNext())
        }

        queryResult.close()
    }

    fun deleteCategory(categoryId: Long){
        val db = writableDatabase
        db.delete(TABLE_MEAL_ITEM, "$COL_MEAL_ITEM_ID=?", arrayOf(categoryId.toString()))
        db.delete(TABLE_CATEGORY, "$COL_ID=?", arrayOf(categoryId.toString()))
    }

    fun addCategory(category : Category) :Boolean {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, category.name)
        cv.put(COL_MADEBY, category.owner)
        val result = db.insert(TABLE_CATEGORY, null, cv)
        return result != (-1).toLong()
    }

    fun updateCategory(category : Category)  {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, category.name)
        cv.put(COL_MADEBY, category.owner)
        db.update(TABLE_CATEGORY, cv, "$COL_ID=?", arrayOf(category.id.toString()))
    }

    fun getCategories(USERID : String) : MutableList<Category> {
        val result : MutableList<Category> = ArrayList()
        val db = readableDatabase
        val queryResult = db.rawQuery("SELECT * from $TABLE_CATEGORY  ", null)

        if(queryResult.moveToFirst()){
            do {
                val category = Category()
                category.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                category.name = queryResult.getString(queryResult.getColumnIndex(COL_NAME))
                category.owner = queryResult.getString(queryResult.getColumnIndex(COL_MADEBY))
                result.add(category)
            } while(queryResult.moveToNext())
        }

        queryResult.close()
        return result
    }

    fun addMealItem(item : MealItem) : Boolean {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_MEAL_ITEM_NAME, item.name)
        cv.put(COL_MEAL_ITEM_ID, item.MealItemId)
        cv.put(COL_IS_COMPLETED, item.isCompleted)

        val result = db.insert(TABLE_MEAL_ITEM, null, cv)
        return result != (-1).toLong()
    }

    fun getMealItems(mealItemId : Long) : MutableList<MealItem> {
        val result : MutableList<MealItem> = ArrayList()

        val db = readableDatabase
        val queryResult = db.rawQuery("SELECT * FROM $TABLE_MEAL_ITEM WHERE $COL_MEAL_ITEM_ID=$mealItemId", null)

        if (queryResult.moveToFirst()) {
            do {
                val item = MealItem()
                item.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                item.MealItemId = queryResult.getLong(queryResult.getColumnIndex(COL_MEAL_ITEM_ID))
                item.name = queryResult.getString(queryResult.getColumnIndex(COL_MEAL_ITEM_NAME))
                item.isCompleted =
                    queryResult.getInt(queryResult.getColumnIndex(COL_IS_COMPLETED)) == 1
                item.MealItemId = mealItemId
                result.add(item)
            } while (queryResult.moveToNext())
        }

        queryResult.close()
        return result
    }

    fun updateMealItem(item: MealItem) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_MEAL_ITEM_NAME, item.name)
        cv.put(COL_MEAL_ITEM_ID, item.MealItemId)
        cv.put(COL_IS_COMPLETED, item.isCompleted)

        db.update(TABLE_MEAL_ITEM, cv, "$COL_ID=?", arrayOf(item.id.toString()))

    }
}
