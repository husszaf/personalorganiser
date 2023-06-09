package com.example.personalorganiser

import Todo
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(
    private val todoManager: TodoManager
): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    private var todos: List<Todo> = todoManager.getAllTodos()

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTodoTitle: TextView = itemView.findViewById(R.id.tvTodoTitle)
        val cbDone: CheckBox = itemView.findViewById(R.id.cbDone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    fun addTodo(todo: Todo) {
        todoManager.addTodo(todo)
        todos = todoManager.getAllTodos()
        notifyDataSetChanged()
    }

    fun deleteDoneTodos() {
        todoManager.deleteDoneTodos()
        todos = todoManager.getAllTodos()
        notifyDataSetChanged()
    }

    private fun toggleStrikeThrough(tvTodoTitle: TextView, isChecked: Boolean) {
        if(isChecked) {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val curTodo = todos[position]
        holder.itemView.apply {
            holder.tvTodoTitle.text = curTodo.title
            holder.cbDone.isChecked = curTodo.isChecked
            toggleStrikeThrough(holder.tvTodoTitle, curTodo.isChecked)
            holder.cbDone.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(holder.tvTodoTitle, isChecked)
                curTodo.isChecked = !curTodo.isChecked
                todoManager.updateTodoById(curTodo.id, curTodo.title, curTodo.isChecked)
            }
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }

}