package com.example.checkoff

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class AddDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME= "taskapp.db"
        private const val DATABASE_VERSION= 1
        private const val TABLE_NAME= "alltasks"
        private const val COLUMN_ID= "id"
        private const val COLOUMN_TITLE= "title"
        private const val COLOUMN_CONTENT= "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLOUMN_TITLE TEXT, $COLOUMN_CONTENT TEXT)"
        db?.execSQL(createTableQuery)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertTask(task: Task){
        val db = writableDatabase
        val values = contentValuesOf().apply {
            put(COLOUMN_TITLE, task.title)
            put(COLOUMN_CONTENT, task.content)
        }
        db.insert(TABLE_NAME, null,values)
        db.close()

    }

    fun getAllTasks(): List<Task> {
        val TaskList = mutableListOf<Task>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while(cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLOUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLOUMN_CONTENT))

            val task = Task(id, title,content)
            TaskList.add(task)
        }
        cursor.close()
        db.close()
        return  TaskList
    }
    fun updateTask(task: Task){
        val db = writableDatabase
        val values = ContentValues().apply{
            put(COLOUMN_TITLE, task.title)
            put(COLOUMN_CONTENT, task.content)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(task.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getTaskByID(taskId: Int): Task{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $taskId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLOUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLOUMN_CONTENT))



        cursor.close()
        db.close()
        return Task(id, title, content)

    }


    fun deleteTask(taskId:  Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(taskId.toString())
        db.delete(TABLE_NAME,whereClause,whereArgs)
        db.close()
    }







}