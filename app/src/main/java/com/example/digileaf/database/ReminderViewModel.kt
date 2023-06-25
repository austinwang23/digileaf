package com.example.digileaf.database

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.digileaf.model.Reminder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

class ReminderViewModel: ViewModel() {
    var reminderItems = MutableLiveData<MutableList<Reminder>?>()

    init {
        reminderItems.value = mutableListOf()
    }

    fun addReminder(newReminder: Reminder) {
        val list = reminderItems.value
        list!!.add(newReminder)
        reminderItems.postValue(list)
    }

    fun updateReminder(id: UUID, title: String, desc: String, dueTime: LocalTime?) {
        val list = reminderItems.value
        val reminder = list!!.find{ it.id == id }!!
        reminder.title = title
        reminder.desc = desc
        reminder.dueTime = dueTime
        reminderItems.postValue(list)
    }

    fun setCompleted(reminderItem: Reminder) {
        val list = reminderItems.value
        val reminder = list!!.find { it.id == reminderItem.id }!!
        if (reminderItem.completedDate == null) {
            reminderItem.completedDate = LocalDate.now()
        }
        reminderItems.postValue(list)
    }
}