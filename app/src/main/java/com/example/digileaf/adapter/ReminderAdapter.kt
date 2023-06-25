package com.example.digileaf.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.R
import com.example.digileaf.databinding.ReminderCellBinding
import com.example.digileaf.model.Reminder

class ReminderAdapter(
    private val reminderList: ArrayList<Reminder>,
    private val clickListener: ReminderClickListener
) : RecyclerView.Adapter<ReminderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val from = LayoutInflater.from(parent.context)
        var binding = ReminderCellBinding.inflate(from, parent, false)
        return ReminderViewHolder(parent.context, binding, clickListener)
    }

    override fun getItemCount(): Int {
        return reminderList.size
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bindReminder(reminderList[position])
    }

}