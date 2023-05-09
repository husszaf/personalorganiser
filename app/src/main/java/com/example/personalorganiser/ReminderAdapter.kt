package com.example.personalorganiser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat

class ReminderAdapter(private val reminders: MutableList<Reminder>) : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    //Responsible to inflate the layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        // Bind the data to the views in the ViewHolder
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        return ReminderViewHolder(view)
    }

    // This method is called for each item in the RecyclerView, and is responsible for binding the data to the views
    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(reminders[position])
        // Set up an on long click listener for the item view
        holder.itemView.setOnLongClickListener {
            val context = holder.itemView.context
            AlertDialog.Builder(context)
                .setTitle("Delete Reminder")
                .setMessage("Are you sure you want to delete this reminder?")
                .setPositiveButton("Yes") { _, _ ->
                    // If user confirms deletion, delete the reminder from the database and the list, and notify the adapter
                    val dbHelper = ReminderDatabaseHelper(context)
                    val db = dbHelper.writableDatabase
                    val selection = "${ReminderDatabaseHelper.COLUMN_ID} = ?"
                    val selectionArgs = arrayOf(reminders[position].id.toString())
                    db.delete(ReminderDatabaseHelper.TABLE_REMINDER, selection, selectionArgs)
                    reminders.removeAt(position)
                    notifyDataSetChanged()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
            true
        }
    }

    // This method returns the number of items in the RecyclerView
    override fun getItemCount(): Int {
        return reminders.size
    }
    // This is a ViewHolder class that holds references to the views in the item layout
    class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // This method is responsible for binding the Reminder data to the views in the ViewHolder
        fun bind(reminder: Reminder) {
            itemView.findViewById<TextView>(R.id.tvReminderText).text = reminder.text
            itemView.findViewById<TextView>(R.id.tvReminderDateTime).text = DateFormat.getDateInstance(DateFormat.MEDIUM).format(reminder.dateTime)
            itemView.findViewById<TextView>(R.id.tvReminderTime).text = DateFormat.getTimeInstance(DateFormat.SHORT).format(reminder.dateTime)
        }
    }
}
