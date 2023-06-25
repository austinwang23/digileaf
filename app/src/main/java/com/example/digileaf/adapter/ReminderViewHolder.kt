package com.example.digileaf.adapter

import android.content.Context
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.databinding.ReminderCellBinding
import com.example.digileaf.entities.Reminder
import java.time.format.DateTimeFormatter

class ReminderViewHolder(
    private val context: Context,
    private val binding: ReminderCellBinding,
    private val clickListener: ReminderClickListener
): RecyclerView.ViewHolder(binding.root) {
    private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

    fun bindReminder(reminder: Reminder) {
        binding.title.text = reminder.title

        // strike through completed reminders
        if (reminder.isCompleted()) {
            binding.title.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.dueTime.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        binding.completeButton.setImageResource(reminder.imageResource())
        binding.completeButton.setColorFilter(reminder.imageColor(context))
        binding.completeButton.setOnClickListener{
            clickListener.completeReminder(reminder)
        }
        binding.reminderCellContainer.setOnClickListener{
            clickListener.editReminder(reminder)
        }

        if (reminder.dueTime() != null) {
            binding.dueTime.text = timeFormat.format(reminder.dueTime())
        }
        else {
            binding.dueTime.text = ""
        }
    }
}