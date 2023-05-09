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

        //get reminders from database
        val reminders = getRemindersFromDatabase()

        //set up recycler view
        val reminderAdapter = ReminderAdapter(reminders)
        val recyclerView = findViewById<RecyclerView>(R.id.remindersRecyclerView)
        recyclerView.adapter = reminderAdapter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Show empty list message if there are no reminders
        emptyReminderTextView = findViewById(R.id.empty_reminder_text)
        if (reminders.isEmpty()) {
            emptyReminderTextView.visibility = View.VISIBLE
        } else {
            emptyReminderTextView.visibility = View.GONE
        }
    }

    // Query the database for reminders and convert the result to a list of Reminder objects
    private fun getRemindersFromDatabase(): MutableList<Reminder> {
        val dbHelper = ReminderDatabaseHelper(this)
        val db = dbHelper.readableDatabase

        // Define the columns to retrieve from the table
        val projection = arrayOf(
            ReminderDatabaseHelper.COLUMN_ID,
            ReminderDatabaseHelper.COLUMN_TEXT,
            ReminderDatabaseHelper.COLUMN_DATE_TIME
        )

        // Sort the results by date/time in ascending order
        val sortOrder = "${ReminderDatabaseHelper.COLUMN_DATE_TIME} ASC"

        // Query the table for all records, order by date/time
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

        // Iterate through the cursor to create a list of Reminder objects
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