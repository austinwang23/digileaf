package com.example.digileaf.model

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.digileaf.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

class Reminder(
    var title: String,
    var desc : String,
    var dueTime: LocalTime?,
    var completedDate: LocalDate?,
    val id: UUID = UUID.randomUUID()
) {
    fun isCompleted(): Boolean {
        return completedDate != null
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

}