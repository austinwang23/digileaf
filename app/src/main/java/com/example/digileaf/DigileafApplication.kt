package com.example.digileaf

import android.app.Application
import com.example.digileaf.database.AppDatabase
import com.example.digileaf.database.PlantRepository

class DigileafApplication : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
    val plantRepository by lazy { PlantRepository(database.plantDao()) }
}