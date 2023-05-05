package com.example.personalorganiser

import Todo
import TodoDatabaseHelper
import android.content.ContentValues
import android.content.Context

class TodoManager(private val context: Context) {

    private val dbHelper = TodoDatabaseHelper(context)

    fun addTodo(todo: Todo) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TodoDatabaseHelper.COLUMN_TITLE, todo.title)
            put(TodoDatabaseHelper.COLUMN_CHECKED, todo.isChecked)
        }
        db.insert(TodoDatabaseHelper.TABLE_TODO, null, values)
        db.close()
    }

    fun deleteTodoById(id: Long) {
        val db = dbHelper.writableDatabase
        db.delete(TodoDatabaseHelper.TABLE_TODO, "${TodoDatabaseHelper.COLUMN_ID} = ?", arrayOf(id.toString()))
        db.close()
    }

    fun getAllTodos(): List<Todo> {
        val todos = mutableListOf<Todo>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TodoDatabaseHelper.TABLE_TODO,
            arrayOf(TodoDatabaseHelper.COLUMN_ID, TodoDatabaseHelper.COLUMN_TITLE, TodoDatabaseHelper.COLUMN_CHECKED),
            null,
            null,
            null,
            null,
            null
        )
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(TodoDatabaseHelper.COLUMN_ID))
                val title = getString(getColumnIndexOrThrow(TodoDatabaseHelper.COLUMN_TITLE))
                val isChecked = getInt(getColumnIndexOrThrow(TodoDatabaseHelper.COLUMN_CHECKED)) != 0
                todos.add(Todo(id, title, isChecked))
            }
        }
        cursor.close()
        db.close()
        return todos
    }

    fun deleteDoneTodos() {
        val db = dbHelper.writableDatabase
        db.delete(TodoDatabaseHelper.TABLE_TODO, "${TodoDatabaseHelper.COLUMN_CHECKED} = ?", arrayOf("1"))
        db.close()
    }

    fun updateTodoById(id: Long, title: String, isChecked: Boolean) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TodoDatabaseHelper.COLUMN_TITLE, title)
            put(TodoDatabaseHelper.COLUMN_CHECKED, isChecked)
        }
        db.update(TodoDatabaseHelper.TABLE_TODO, values, "${TodoDatabaseHelper.COLUMN_ID} = ?", arrayOf(id.toString()))
        db.close()
    }


}
