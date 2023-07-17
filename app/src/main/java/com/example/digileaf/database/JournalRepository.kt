package com.example.digileaf.database

import androidx.annotation.WorkerThread
import com.example.digileaf.dao.JournalDao
import com.example.digileaf.entities.Journal
import kotlinx.coroutines.flow.Flow

class JournalRepository(private val journalDao: JournalDao) {
    val allJournals: Flow<List<Journal>> = journalDao.getAll()
    fun allJournalsByPlantId(plantId: Int) : Flow<List<Journal>> {
        return journalDao.getAllByPlantId(plantId)
    }

    fun allJournalDates() : Flow<List<String>> {
        return journalDao.getAllJournalDates()
    }

    fun allJournalsByDate(Date: String) : Flow<List<Journal>> {
        return journalDao.getAllByDate(Date)
    }

    // suspended function are performed asynchronously by default
    // suspended functions do not violate no main thread db rule
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(journal: Journal) {
        journalDao.insertJournal(journal)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getJournalCount(): Int {
        return journalDao.getJournalCount()
    }
}