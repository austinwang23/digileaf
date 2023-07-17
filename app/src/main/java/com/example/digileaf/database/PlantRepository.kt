package com.example.digileaf.database

import androidx.annotation.WorkerThread
import com.example.digileaf.dao.PlantDao
import com.example.digileaf.entities.Plant
import com.example.digileaf.entities.PlantStatus
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

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updatePlantStatus(plantStatus: PlantStatus) {
        plantDao.updatePlantStatus(plantStatus)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getPlantCount(): Int {
        return plantDao.getPlantCount()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getWateredCount(): Int {
        return plantDao.getWateredCount()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getFertilizedCount(): Int {
        return plantDao.getFertilizedCount()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getGroomedCount(): Int {
        return plantDao.getGroomedCount()
    }

}