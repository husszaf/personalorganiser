package com.example.personalorganiser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat

class ReminderAdapter(private val reminders: MutableList<Reminder>) : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(reminders[position])
        holder.itemView.setOnLongClickListener {
            val context = holder.itemView.context
            AlertDialog.Builder(context)
                .setTitle("Delete Reminder")
                .setMessage("Are you sure you want to delete this reminder?")
                .setPositiveButton("Yes") { _, _ ->
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

    override fun getItemCount(): Int {
        return reminders.size
    }

    class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(reminder: Reminder) {
            itemView.findViewById<TextView>(R.id.tvReminderText).text = reminder.text
            itemView.findViewById<TextView>(R.id.tvReminderDateTime).text = DateFormat.getDateInstance(DateFormat.MEDIUM).format(reminder.dateTime)
            itemView.findViewById<TextView>(R.id.tvReminderTime).text = DateFormat.getTimeInstance(DateFormat.SHORT).format(reminder.dateTime)
        }
    }
}
