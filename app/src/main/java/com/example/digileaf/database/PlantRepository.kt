package com.example.digileaf.database

import androidx.annotation.WorkerThread
import com.example.digileaf.dao.PlantDao
import com.example.digileaf.entities.Plant
import kotlinx.coroutines.flow.Flow

class PlantRepository(private val plantDao: PlantDao) {
    val allPlants: Flow<List<Plant>> = plantDao.getAll()

    // suspended function are performed asynchronously by default
    // suspended functions do not violate no main thread db rule
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(plant: Plant) {
        plantDao.insertPlant(plant)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(plant: Plant) {
        plantDao.delete(plant)
    }
}