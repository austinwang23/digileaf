package com.example.digileaf.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.digileaf.entities.Reminder
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class ReminderViewModel(private val repository: ReminderRepository): ViewModel() {
    var reminderItems: LiveData<List<Reminder>> =repository.allReminders.asLiveData()

    fun addReminder(newReminder: Reminder) = viewModelScope.launch {
        repository.insertReminder(newReminder)
    }

    fun updateReminder(reminder: Reminder) = viewModelScope.launch {
        repository.updateReminder(reminder)
    }

    fun setCompleted(reminder: Reminder) = viewModelScope.launch {
        if (!reminder.isCompleted()) {
            reminder.completedDateString = Reminder.dateFormatter.format(LocalDate.now())
        }
        repository.updateReminder(reminder)
    }
}

class ReminderModelFactory(private val repository: ReminderRepository): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReminderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReminderViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown class for Reminder View Model")
    }
}