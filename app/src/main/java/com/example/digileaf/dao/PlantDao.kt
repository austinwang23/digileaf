package com.example.digileaf.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.digileaf.entities.Plant
import com.example.digileaf.entities.PlantStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    @Insert
    // Can consider using this for random seed data?
    suspend fun insertAll(vararg plants: Plant)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: Plant)

    @Update(entity = Plant::class)
    suspend fun updatePlantStatus(vararg plantStatus: PlantStatus)

    @Delete
    suspend fun delete(plant: Plant)

    @Query("SELECT * FROM plants ORDER BY id DESC")
    fun getAll(): Flow<List<Plant>>
}