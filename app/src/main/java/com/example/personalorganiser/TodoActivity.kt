package com.example.personalorganiser

import Todo
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TodoActivity : AppCompatActivity() {

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var rvTodoItems: RecyclerView
    private lateinit var btnAddTodo: Button
    private lateinit var btnDeleteDoneTodos: Button
    private lateinit var etTodoTitle: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_main)

        // Initialize the views
        rvTodoItems = findViewById(R.id.rvTodoItems)
        btnAddTodo = findViewById(R.id.btnAddTodo)
        btnDeleteDoneTodos = findViewById(R.id.btnDeleteDoneTodos)
        etTodoTitle = findViewById(R.id.etTodoTitle)

        val todoManager = TodoManager(this)
        todoAdapter = TodoAdapter(todoManager)

        rvTodoItems.adapter = todoAdapter
        rvTodoItems.layoutManager = LinearLayoutManager(this)

        btnAddTodo.setOnClickListener{
            val todoTitle = etTodoTitle.text.toString()
            if(todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle)
                todoAdapter.addTodo(todo)
                etTodoTitle.text.clear()
            }
        }
        btnDeleteDoneTodos.setOnClickListener {
            todoAdapter.deleteDoneTodos()
        }
    }
}
