package com.example.digileaf.adapter

import com.example.digileaf.entities.Reminder

interface ReminderClickListener {
    fun editReminder(reminder: Reminder)
    fun completeReminder(reminder: Reminder)
}