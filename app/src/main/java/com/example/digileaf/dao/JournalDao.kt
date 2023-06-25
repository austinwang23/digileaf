package com.example.digileaf.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.digileaf.entities.Journal
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Insert
    // Can consider using this for random seed data?
    suspend fun insertAll(vararg journals: Journal)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournal(journal: Journal)

    @Delete
    suspend fun delete(journal: Journal)

    @Query("SELECT * FROM journals")
    fun getAll(): Flow<List<Journal>>

    @Query("SELECT * FROM journals WHERE plant_id = :plantId ORDER BY id DESC")
    fun getAllByPlantId(plantId: Int): Flow<List<Journal>>

    @Query("SELECT DISTINCT date FROM journals")
    fun getAllJournalDates(): Flow<List<String>>

    @Query("SELECT * FROM journals WHERE date = :date")
    fun getAllByDate(date: String): Flow<List<Journal>>
}