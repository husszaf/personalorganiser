package com.example.personalorganiser

import Todo
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TodoActivity : AppCompatActivity() {

    //Declaration of variables and views
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

        // Create a new instance of the TodoManager
        val todoManager = TodoManager(this)

        // Create a new instance of the TodoAdapter with the TodoManager
        todoAdapter = TodoAdapter(todoManager)

        // Set the TodoAdapter to the RecyclerView
        rvTodoItems.adapter = todoAdapter
        // Set the layout manager for the RecyclerView
        rvTodoItems.layoutManager = LinearLayoutManager(this)

        // Add an OnClickListener for the Add button
        btnAddTodo.setOnClickListener{
            // Get the title of the new to-do from the EditText
            val todoTitle = etTodoTitle.text.toString()
            // Check if the todo title is not empty
            if(todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle)
                todoAdapter.addTodo(todo)
                etTodoTitle.text.clear()
            }
        }

        // Add an OnClickListener for the Delete button
        btnDeleteDoneTodos.setOnClickListener {
            // Remove all completed todos from the adapter
            todoAdapter.deleteDoneTodos()
        }
    }
}
