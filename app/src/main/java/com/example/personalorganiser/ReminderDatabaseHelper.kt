package com.example.personalorganiser

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class ReminderDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        public const val DATABASE_NAME = "reminder.db"
        public const val DATABASE_VERSION = 1

        public const val TABLE_REMINDER = "reminder"
        public const val COLUMN_ID = "_id"
        public const val COLUMN_TEXT = "text"
        public const val COLUMN_DATE_TIME = "date_time"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSql = "CREATE TABLE $TABLE_REMINDER ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TEXT TEXT, $COLUMN_DATE_TIME TEXT)"
        db.execSQL(createTableSql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val dropTableSql = "DROP TABLE IF EXISTS $TABLE_REMINDER"
        db.execSQL(dropTableSql)
        onCreate(db)
    }

    fun addReminder(reminder: Reminder) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TEXT, reminder.text)
            put(COLUMN_DATE_TIME, SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(reminder.dateTime))
        }
        db.insert(TABLE_REMINDER, null, values)
        db.close()
    }

}
