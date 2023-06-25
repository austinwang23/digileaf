package com.example.digileaf

import android.app.Application
import com.example.digileaf.database.AppDatabase
import com.example.digileaf.database.JournalRepository
import com.example.digileaf.database.PlantRepository
import com.example.digileaf.database.ReminderRepository

class DigileafApplication : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
    val plantRepository by lazy { PlantRepository(database.plantDao()) }
    val journalRepository by lazy {JournalRepository(database.journalDao())}
    val reminderRepository by lazy {ReminderRepository(database.reminderDao())}
}