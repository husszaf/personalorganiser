import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TodoDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "todoDB.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE todoDB (id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS todoDB")
        onCreate(db)
    }

    fun addTodoItem(todo: Todo) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", todo.title)
            put("isChecked", if (todo.isChecked) 1 else 0)
        }
        db.insert("todoDB", null, values)
        db.close()
    }


/*
    fun addTodoItem(todo: Todo): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_TITLE, todo.title)
        values.put(COL_CHECKED, if (todo.isChecked) 1 else 0)
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }
*/
    fun updateTodoItem(todo: Todo): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("title", todo.title)
        values.put("isChecked", if (todo.isChecked) 1 else 0)
        val rowsAffected = db.update("todoDB", values, "id=?", arrayOf(todo.id.toString()))
        db.close()
        return rowsAffected
    }

    fun deleteTodoItem(todo: Todo) {
        val db = this.writableDatabase
        db.delete("todoDB", "id=?", arrayOf(todo.id.toString()))
        db.close()
    }

    fun getAllTodoItems(): MutableList<Todo> {
        val todos = mutableListOf<Todo>()
        val db = this.readableDatabase
        val query = "SELECT * FROM todoDB"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val id = result.getLong(result.getColumnIndexOrThrow("id"))
                val title = result.getString(result.getColumnIndexOrThrow("title"))
                val checked = result.getInt(result.getColumnIndexOrThrow("isChecked")) == 1
                todos.add(Todo(id, title, checked))
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return todos
    }
}