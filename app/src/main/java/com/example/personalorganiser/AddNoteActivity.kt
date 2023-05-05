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

    private lateinit var noteEditText: EditText
    private lateinit var databaseHelper: NotesDatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        noteEditText = findViewById(R.id.note_edit_text)
        databaseHelper = NotesDatabaseHelper(this)

        noteEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                saveNote()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener {
            saveNote()
        }

    }

    private fun saveNote() {
        val noteText = noteEditText.text.toString()
        if(noteText == ""){
            Toast.makeText(this, "Note is empty", Toast.LENGTH_SHORT).show()
        }
        else{
            val note = Note(0, noteText) // 0 is a placeholder for the auto-generated ID
            databaseHelper.addNote(note)
            Toast.makeText(this, "Note saved: $noteText", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, NoteEditorActivity::class.java))
            finish()
        }
    }
}
