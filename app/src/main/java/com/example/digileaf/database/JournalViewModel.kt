package com.example.digileaf.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.digileaf.entities.Journal
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
    fun insert(journal: Journal) = viewModelScope.launch {
        repository.insert(journal)
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