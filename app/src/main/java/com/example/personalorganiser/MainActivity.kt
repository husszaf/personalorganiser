package com.example.personalorganiser

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        //Every time the app is launched or refreshed it will show a Toast
        Toast.makeText(this, "App Initialized", Toast.LENGTH_SHORT).show()

        //When tapped on the note button
        val noteButton = findViewById<Button>(R.id.note)
        noteButton.setOnClickListener {
            val intent = Intent(this, NoteEditorActivity::class.java)
            startActivity(intent)
        }

        //Schedule
        val scheduleButton = findViewById<Button>(R.id.schedule)
        scheduleButton.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }
        
        //reminder
        val reminderButton = findViewById<Button>(R.id.reminder)
        reminderButton.setOnClickListener { 
            val intent = Intent(this, TodoActivity::class.java)
            startActivity(intent)
        }


        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            // create and show the popup dialog
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("App")
            builder.setMessage("What would you like to open?")
            builder.setPositiveButton("Write a note") { _: DialogInterface, _: Int ->
                // do nothing, just dismiss the dialog
                //dialog.dismiss()
                val intent = Intent(this, AddNoteActivity::class.java)
                startActivity(intent)
            }
            builder.setNegativeButton("Close") { dialog: DialogInterface, _: Int ->
                // do nothing, just dismiss the dialog
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

    }

    override fun onBackPressed(){
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("App")
        builder.setMessage("Would you like to quit the app?")
        builder.setPositiveButton("Quit") { dialog: DialogInterface, _: Int ->
            // do nothing, just dismiss the dialog
            //dialog.dismiss()
            finish()
        }
        builder.setNegativeButton("Close") { dialog: DialogInterface, _: Int ->
            // do nothing, just dismiss the dialog
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

}