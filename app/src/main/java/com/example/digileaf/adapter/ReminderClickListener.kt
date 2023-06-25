package com.example.digileaf.adapter

import com.example.digileaf.model.Reminder

interface ReminderClickListener {
    fun editReminder(reminder: Reminder)
    fun completeReminder(reminder: Reminder)
}