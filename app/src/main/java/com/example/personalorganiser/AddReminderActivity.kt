package com.example.personalorganiser

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class AddReminderActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private lateinit var etReminderText: EditText
    private lateinit var btnPickDate: Button
    private lateinit var btnPickTime: Button
    private lateinit var btnAddReminder: Button
    private lateinit var btnViewReminder: Button
    private lateinit var selectedDate: TextView
    private lateinit var selectedTime: TextView

    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        etReminderText = findViewById(R.id.etReminderText)
        btnPickDate = findViewById(R.id.btnSelectDate)
        btnPickTime = findViewById(R.id.btnSelectTime)
        btnAddReminder = findViewById(R.id.btnAddReminder)
        btnViewReminder = findViewById(R.id.btnViewReminder)

        selectedDate = findViewById(R.id.selectedDate)
        selectedTime = findViewById(R.id.selectedTime)


        calendar = Calendar.getInstance()

        btnPickDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(this, this, year, month, day).show()
        }

        btnPickTime.setOnClickListener {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            TimePickerDialog(this, this, hour, minute, true).show()
        }

        btnViewReminder.setOnClickListener {
            intent = Intent(this, ReminderListActivity::class.java)
            startActivity(intent)
        }

        btnAddReminder.setOnClickListener {
            val reminderText = etReminderText.text.toString()
            if (reminderText.isNotEmpty()) {
                val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(calendar.time)

                val values = ContentValues().apply {
                    put(ReminderDatabaseHelper.COLUMN_TEXT, reminderText)
                    put(ReminderDatabaseHelper.COLUMN_DATE_TIME, dateTime)
                }

                val dbHelper = ReminderDatabaseHelper(this)
                val db = dbHelper.writableDatabase
                db.insert(ReminderDatabaseHelper.TABLE_REMINDER, null, values)
                db.close()

                val intent = Intent(this, ReminderListActivity::class.java)
                intent.putExtra("reminderText", reminderText)
                intent.putExtra("dateTime", dateTime)
                startActivity(intent)

                finish()
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateSelectedDateTime()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        updateSelectedDateTime()
    }

    private fun updateSelectedDateTime() {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        selectedDate.text = "Selected date: " + dateFormat.format(calendar.time)
        selectedTime.text = "Selected Time: " + timeFormat.format(calendar.time)
    }

}
