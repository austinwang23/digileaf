package com.example.digileaf.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.digileaf.entities.Plant
import kotlinx.coroutines.launch

class PlantViewModel(private val repository: PlantRepository) : ViewModel() {

    // reference android demo
    // https://github.com/android/codelab-android-room-with-a-view/blob/kotlin/app/src/main/java/com/example/android/roomwordssample/WordViewModel.kt
    val allPlants: LiveData<List<Plant>> = repository.allPlants.asLiveData()

    // Do through coroutine --> by default no database operations are allowed on main thread
    fun insert(plant: Plant) = viewModelScope.launch {
        repository.insert(plant)
    }

    fun delete(plant: Plant) = viewModelScope.launch {
        repository.delete(plant)
    }
}

class PlantViewModelFactory(private val repository: PlantRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlantViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlantViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}