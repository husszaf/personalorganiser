package com.example.personalorganiser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat

class ReminderAdapter(private val reminders: List<Reminder>) : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(reminders[position])
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
