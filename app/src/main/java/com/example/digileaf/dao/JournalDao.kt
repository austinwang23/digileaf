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
    fun insertAll(vararg journals: Journal)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJournal(journal: Journal)

    @Delete
    fun delete(journal: Journal)

    @Query("SELECT * FROM journals")
    fun getAll(): Flow<List<Journal>>

    @Query("SELECT * FROM journals WHERE plantId = :plantId")
    fun getAllByPlantId(plantId: Int): Flow<List<Journal>>
}