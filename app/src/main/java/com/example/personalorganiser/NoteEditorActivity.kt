package com.example.personalorganiser

import NotesDatabaseHelper
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NoteEditorActivity : AppCompatActivity() {

    private lateinit var addNoteButton: FloatingActionButton
    private lateinit var noteListView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var notes: MutableList<Note>
    private lateinit var databaseHelper: NotesDatabaseHelper
    private lateinit var emptyNotesTextView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)

        addNoteButton = findViewById(R.id.floatingActionButton2)
        addNoteButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        noteListView = findViewById(R.id.note_list_view)
        databaseHelper = NotesDatabaseHelper(this)
        notes = databaseHelper.getAllNotes().toMutableList()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notes.map { it.text })
        noteListView.adapter = adapter

        emptyNotesTextView = findViewById(R.id.empty_text)
        if (notes.isEmpty()) {
            emptyNotesTextView.visibility = View.VISIBLE
        } else {
            emptyNotesTextView.visibility = View.GONE
        }

        noteListView.setOnItemLongClickListener { _, _, position, _ ->
            val builder = AlertDialog.Builder(this@NoteEditorActivity)
            builder.setTitle("Delete note?")
            builder.setMessage("Would you like to delete note?")
            builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                databaseHelper.deleteNoteById(notes[position].id)
                notes.removeAt(position)
                adapter.remove(adapter.getItem(position))
                adapter.notifyDataSetChanged()
                if (notes.isEmpty()) {
                    emptyNotesTextView.visibility = View.VISIBLE
                }
            }
            builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
            true
        }


    }

    override fun onResume() {
        super.onResume()
        notes = databaseHelper.getAllNotes().toMutableList()
        adapter.clear()
        adapter.addAll(notes.map { it.text })
        adapter.notifyDataSetChanged()
        if (notes.isEmpty()) {
            emptyNotesTextView.visibility = View.VISIBLE
        } else {
            emptyNotesTextView.visibility = View.GONE
        }
    }
}
