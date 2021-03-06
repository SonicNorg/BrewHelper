package com.norg.brewhelper

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.norg.brewhelper.model.From
import com.norg.brewhelper.model.Phase
import com.norg.brewhelper.model.TimedPhase

internal class DBHelper private constructor(context: Context)// конструктор суперкласса
    : SQLiteOpenHelper(context, "BrewHelper", null, 17) {

    companion object {
        private var db: DBHelper? = null
        fun dbHelper(context: Context): DBHelper {
            if (db == null) db = DBHelper(context)
            return db as DBHelper
        }
    }

    private val logTag: String = this.javaClass.simpleName

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(logTag, "--- onCreate database ---")
        // создаем таблицу с полями
        db.execSQL("create table recipes ("
                + "_id integer primary key autoincrement,"
                + "name text,"
                + "duration integer,"
                + "description text,"
                + "CONSTRAINT unique_name UNIQUE (name)"
                + ");")
        db.execSQL("create table phases ("
                + "_id integer primary key autoincrement,"
                + "recipe_id integer NOT NULL,"
                + "name text NOT NULL,"
                + "duration integer,"
                + "delay integer,"
                + "alarm integer,"
                + "start text,"
                + "description text,"
                + "CONSTRAINT unique_parent_name UNIQUE (recipe_id, name),"
                + "FOREIGN KEY(recipe_id) REFERENCES recipes(_id));")
//        do {
//            val rawQuery = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table';", null)
//            rawQuery.moveToFirst()
//            for (i in 0 until rawQuery.columnCount) {
//                Log.d(logTag, rawQuery.getColumnName(i))
//            }
//        } while (rawQuery.moveToNext())

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (newVersion == 12)
            db.execSQL("alter table phases add column alarm integer; alter table phases add column delay integer;")
    }

    fun saveRecipe(recipe: Phase): Long {
        val db = writableDatabase
        val valuesRecipe = ContentValues()
        var id: Long?
        db.beginTransaction()
        try {
            valuesRecipe.put("name", recipe.name)
            valuesRecipe.put("duration", recipe.duration)
            valuesRecipe.put("description", recipe.description)

            id = db.insert("recipes", null, valuesRecipe)
            if (!recipe.phases.isEmpty())
                for (phase in recipe.phases) {
                    val valuesPhase = ContentValues()
                    valuesPhase.put("recipe_id", id)
                    valuesPhase.put("name", phase.name)
                    valuesPhase.put("duration", phase.duration)
                    valuesPhase.put("delay", phase.delay)
                    valuesPhase.put("alarm", if (phase.alarm) 1 else 0)
                    valuesPhase.put("start", phase.start.toString())
                    valuesPhase.put("description", phase.description)

                    db.insert("phases", null, valuesPhase)
                }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
        return id ?: throw DBException()
    }

    fun getRecipes(): List<Phase> {
        val db = readableDatabase
        val result: MutableList<Phase> = ArrayList()
        val cursor = db.query("recipes", arrayOf("name", "duration", "description"), null, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                result.add(Phase(0, cursor.getString(0), cursor.getInt(1), cursor.getString(2)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return result
    }

    fun getRecipesCursor(): Cursor {
        return readableDatabase.query("recipes", arrayOf("_id", "name", "duration", "description"), null, null, null, null, null)
    }

    fun getPhases(recipe: Phase): MutableList<TimedPhase> {
        val db = readableDatabase
        val result: MutableList<TimedPhase> = ArrayList()
        val cursor = db.query("phases", arrayOf("_id", "name", "duration", "delay", "start", "description", "alarm"), "recipe_id=?", arrayOf(recipe.id.toString()), null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                result.add(
                        TimedPhase(recipe,
                                Phase(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getString(5)),
                                cursor.getInt(3),
                                From.valueOf(cursor.getString(4)),
                                cursor.getInt(6) > 0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return result
    }
}