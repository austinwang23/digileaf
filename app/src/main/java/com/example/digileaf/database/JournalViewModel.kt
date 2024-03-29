package com.example.digileaf.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.digileaf.entities.Journal
import com.example.digileaf.helpers.NotificationHelper
import kotlinx.coroutines.launch

class JournalViewModel(private val repository: JournalRepository) : ViewModel() {

    // reference android demo
    // https://github.com/android/codelab-android-room-with-a-view/blob/kotlin/app/src/main/java/com/example/android/roomwordssample/WordViewModel.kt
    val allJournals: LiveData<List<Journal>> = repository.allJournals.asLiveData()
    fun allJournalsByPlantId(plantId: Int) : LiveData<List<Journal>> {
        return repository.allJournalsByPlantId(plantId).asLiveData()
    }

    fun allJournalByDate(date: String) : LiveData<List<Journal>> {
        return repository.allJournalsByDate(date).asLiveData()
    }

    fun allJournalDates(): LiveData<List<String>> {
        return repository.allJournalDates().asLiveData()
    }

    // Do through coroutine --> by default no database operations are allowed on main thread
    fun insert(journal: Journal, context: Context) {
        viewModelScope.launch {
            repository.insert(journal)
            getJournalCount().asFlow().collect {
                if (it == 10 || it == 50 || it == 100) {
                    NotificationHelper.sendAchievementsNotification(context)
                }
            }
        }
    }

    fun getJournalCount(): LiveData<Int> {
        val result = MutableLiveData<Int>()

        viewModelScope.launch {
            result.postValue(repository.getJournalCount())
        }
        return result
    }
}

class JournalViewModelFactory(private val repository: JournalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JournalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JournalViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}