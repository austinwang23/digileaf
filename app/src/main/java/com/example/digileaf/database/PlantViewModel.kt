package com.example.digileaf.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.digileaf.entities.Plant
import com.example.digileaf.entities.PlantStatus
import com.example.digileaf.enums.PlantStatusType
import com.example.digileaf.helpers.NotificationHelper
import kotlinx.coroutines.launch


class PlantViewModel(private val repository: PlantRepository) : ViewModel() {

    // reference android demo
    // https://github.com/android/codelab-android-room-with-a-view/blob/kotlin/app/src/main/java/com/example/android/roomwordssample/WordViewModel.kt
    val allPlants: LiveData<List<Plant>> = repository.allPlants.asLiveData()

    // Do through coroutine --> by default no database operations are allowed on main thread
    fun insert(plant: Plant, context: Context) {
        viewModelScope.launch {
            repository.insert(plant)
            getPlantCount().asFlow().collect {
                if (it == 3 || it == 5 || it == 10) {
                    NotificationHelper.sendAchievementsNotification(context)
                }
            }
        }
    }

    fun delete(plant: Plant) = viewModelScope.launch {
        repository.delete(plant)
    }

    fun updatePlantStatus(plantStatus: PlantStatus, context: Context) = viewModelScope.launch {
        val wateredBefore: Int?
        val fertilizedBefore: Int?
        val groomedBefore: Int?
        wateredBefore = getWateredCount()
        fertilizedBefore = getFertilizedCount()
        groomedBefore = getGroomedCount()

        repository.updatePlantStatus(plantStatus)

        val wateredAfter: Int?
        val fertilizedAfter: Int?
        val groomedAfter: Int?

        wateredAfter = getWateredCount()
        fertilizedAfter = getFertilizedCount()
        groomedAfter = getGroomedCount()

        if (wateredAfter != wateredBefore) {
            checkAndSendAchievementNotification(wateredAfter, PlantStatusType.WATER, context)
        }

        if (fertilizedAfter != fertilizedBefore) {
            checkAndSendAchievementNotification(fertilizedAfter, PlantStatusType.FERTILIZE, context)
        }

        if (groomedAfter != groomedBefore) {
            checkAndSendAchievementNotification(groomedAfter, PlantStatusType.GROOM, context)
        }
    }

    fun getPlantCount(): LiveData<Int> {
        val result = MutableLiveData<Int>()

        viewModelScope.launch {
            result.postValue(repository.getPlantCount())
        }
        return result
    }

    suspend fun getWateredCount(): Int {
        // Use viewModelScope to call the suspend function from the repository
        return repository.getWateredCount()
    }

    suspend fun getFertilizedCount(): Int {
        return repository.getFertilizedCount()
    }

    suspend fun getGroomedCount(): Int {
        return repository.getGroomedCount()
    }

    private fun checkAndSendAchievementNotification(count: Int, statusType: PlantStatusType, context: Context) {
        when (statusType) {
            PlantStatusType.WATER -> {
                if (count == 1 || count == 5 || count == 10) {
                    NotificationHelper.sendAchievementsNotification(context)
                }
            }
            PlantStatusType.FERTILIZE -> {
                if (count == 1 || count == 3 || count == 5) {
                    NotificationHelper.sendAchievementsNotification(context)
                }
            }
            PlantStatusType.GROOM -> {
                if (count == 1 || count == 5 || count == 10) {
                    NotificationHelper.sendAchievementsNotification(context)
                }
            }
            else -> throw IllegalArgumentException("Invalid PlantStatusType: $statusType")
        }
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