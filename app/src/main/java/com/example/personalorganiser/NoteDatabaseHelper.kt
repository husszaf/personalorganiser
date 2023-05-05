import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.personalorganiser.Note

class NotesDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "notes.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE notes (id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS notes")
        onCreate(db)
    }

    fun addNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("text", note.text)
        }
        db.insert("notes", null, values)
        db.close()
    }

    fun getAllNotes(): List<Note> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM notes", null)
        val notes = mutableListOf<Note>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val text = cursor.getString(cursor.getColumnIndexOrThrow("text"))
            notes.add(Note(id, text))
        }
        cursor.close()
        db.close()
        return notes
    }

    fun deleteNoteById(id: Int) {
        val db = writableDatabase
        db.delete("notes", "id = ?", arrayOf(id.toString()))
        db.close()
    }

    fun getNoteById(id: Int): Note? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM notes WHERE id = ?", arrayOf(id.toString()))
        var note: Note? = null
        if (cursor.moveToFirst()) {
            val text = cursor.getString(cursor.getColumnIndexOrThrow("text"))
            note = Note(id, text)
        }
        cursor.close()
        db.close()
        return note
    }

    fun updateNoteById(id: Int, newText: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("text", newText)
        }
        db.update("notes", values, "id = ?", arrayOf(id.toString()))
        db.close()
    }
}
