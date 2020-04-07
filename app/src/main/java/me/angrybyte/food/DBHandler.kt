package me.angrybyte.food

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import me.angrybyte.food.DTS.Category

class DBHandler(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createCategoryTable = "  CREATE TABLE $TABLE_CATEGORY (" +
                "$COL_ID integer PRIMARY KEY AUTOINCREMENT," +
                "$COL_CREATED_AT datetime DEFAULT CURRENT_TIMESTAMP," +
                "$COL_NAME varchar);"
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

    fun addCategory(category : Category) :Boolean {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, category.name)
        val result = db.insert(TABLE_CATEGORY, null, cv)
        return result != (-1).toLong()
    }

    fun getCategories() : MutableList<Category>{
        val result : MutableList<Category> = ArrayList()
        val db = readableDatabase
        val queryResult = db.rawQuery("SELECT * from $TABLE_CATEGORY", null)

        if(queryResult.moveToFirst()){
            do {
                val category = Category()
                category.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                category.name = queryResult.getString(queryResult.getColumnIndex(COL_NAME))
                result.add(category)
            } while(queryResult.moveToNext())
        }

        queryResult.close()
        return result
    }
}
