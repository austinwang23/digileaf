package com.example.digileaf.entities

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.digileaf.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "reminders")
class Reminder(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "desc") var desc : String,
    @ColumnInfo(name = "dueTimeString") var dueTimeString: String?,
    @ColumnInfo(name = "dueDateString") var dueDateString: String?,
    @ColumnInfo(name = "completedDateString") var completedDateString: String?,
    @ColumnInfo(name = "repetitionType") var repetitionType: RepetitionType = RepetitionType.NEVER,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
    fun completedDate(): LocalDate? {
        if (completedDateString == null) {
            return null
        }
        return LocalDate.parse(completedDateString, dateFormatter)
    }

    fun dueTime(): LocalTime? {
        if (dueTimeString == null) {
            return null
        }
        return LocalTime.parse(dueTimeString, timeFormatter)
    }

    fun dueDate(): LocalDate? {
        if (dueDateString == null) {
            return null
        }
        return LocalDate.parse(dueDateString, dateFormatter)
    }

    fun isCompleted(): Boolean {
        return completedDate() != null
    }

    fun imageResource(): Int {
        if (isCompleted()) {
            return R.drawable.ic_checked_radio
        }
        return R.drawable.ic_unchecked_radio
    }

    fun imageColor(context: Context): Int {
        if (isCompleted()) {
            return ContextCompat.getColor(context, R.color.teal)
        }
        return ContextCompat.getColor(context, R.color.black)
    }

    companion object {
        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_TIME
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE
    }
}