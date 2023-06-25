package com.example.digileaf.database

import androidx.annotation.WorkerThread
import com.example.digileaf.dao.ReminderDao
import com.example.digileaf.entities.Reminder
import kotlinx.coroutines.flow.Flow

class ReminderRepository(private val reminderDao: ReminderDao) {
    val allReminders: Flow<List<Reminder>> = reminderDao.allReminders()

    @WorkerThread
    suspend fun insertReminder(reminder: Reminder) {
        reminderDao.insertReminder(reminder)
    }

    @WorkerThread
    suspend fun updateReminder(reminder: Reminder) {
         reminderDao.updateReminder(reminder)
    }

    @WorkerThread
    suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.deleteReminder(reminder)
    }
}