package com.example.personalorganiser

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class ReminderListActivity : AppCompatActivity() {

    private lateinit var emptyReminderTextView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_list)

        val reminders = getRemindersFromDatabase()

        val reminderAdapter = ReminderAdapter(reminders)
        val recyclerView = findViewById<RecyclerView>(R.id.remindersRecyclerView)
        recyclerView.adapter = reminderAdapter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        emptyReminderTextView = findViewById(R.id.empty_reminder_text)
        if (reminders.isEmpty()) {
            emptyReminderTextView.visibility = View.VISIBLE
        } else {
            emptyReminderTextView.visibility = View.GONE
        }
    }

    private fun getRemindersFromDatabase(): MutableList<Reminder> {
        val dbHelper = ReminderDatabaseHelper(this)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            ReminderDatabaseHelper.COLUMN_ID,
            ReminderDatabaseHelper.COLUMN_TEXT,
            ReminderDatabaseHelper.COLUMN_DATE_TIME
        )

        val sortOrder = "${ReminderDatabaseHelper.COLUMN_DATE_TIME} ASC"

        val cursor = db.query(
            ReminderDatabaseHelper.TABLE_REMINDER,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )

        val reminders = mutableListOf<Reminder>()

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(ReminderDatabaseHelper.COLUMN_ID))
                val text = getString(getColumnIndexOrThrow(ReminderDatabaseHelper.COLUMN_TEXT))
                val dateTimeString = getString(getColumnIndexOrThrow(ReminderDatabaseHelper.COLUMN_DATE_TIME))
                val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateTimeString)!!
                val reminder = Reminder(id, text, dateTime)
                reminders.add(reminder)
            }
        }

        cursor.close()
        return reminders
    }
}