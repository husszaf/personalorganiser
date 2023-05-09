package com.example.personalorganiser

import NotesDatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddNoteActivity : AppCompatActivity() {

    //Declaration of type for Edittext and Database variables
    private lateinit var noteEditText: EditText
    private lateinit var databaseHelper: NotesDatabaseHelper

    // sets the layout to display UI elements
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        // variables that initialize UI elements and database
        noteEditText = findViewById(R.id.note_edit_text)
        databaseHelper = NotesDatabaseHelper(this)

        //Click listener that checks if the View is pressed
        noteEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                saveNote()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        // Checks if the button is pressed then it will call the saveNote function
        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener {
            saveNote()
        }

    }

    //Function that saves the note
    private fun saveNote() {
        val noteText = noteEditText.text.toString()  // get the text from the noteEditText

        if(noteText == ""){
            // display a toast message if the note is empty
            Toast.makeText(this, "Note is empty", Toast.LENGTH_SHORT).show()
        }
        else{
            // create a new note object and add it to the database
            val note = Note(0, noteText) // 0 is a placeholder for the auto-generated ID
            databaseHelper.addNote(note)

            // display a toast message to confirm the note was saved
            Toast.makeText(this, "Note saved: $noteText", Toast.LENGTH_SHORT).show()

            // navigate to the NoteEditorActivity and finish the current activity
            startActivity(Intent(this, NoteEditorActivity::class.java))
            finish()
        }
    }
}
