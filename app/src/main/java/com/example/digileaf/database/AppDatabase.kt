package com.example.digileaf.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.digileaf.dao.PlantDao
import com.example.digileaf.entities.Plant

@Database(entities = [Plant::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            // This builds an empty db
            // return Room.databaseBuilder(context, AppDatabase::class.java, "digileaf-db").build()
            // Seed database if necessary
            return Room.databaseBuilder(context, AppDatabase::class.java, "digileaf-db")
                .addCallback(
                   PrepopulateData(context)
                )
                // wipe and rebuild db instead of migrating
                // we shouldn't need to migrate data, we can just seed with our own test data
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}