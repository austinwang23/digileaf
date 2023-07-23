package com.example.digileaf.helpers

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.digileaf.Notification
import com.example.digileaf.channelID
import com.example.digileaf.entities.Reminder
import com.example.digileaf.entities.RepetitionType
import com.example.digileaf.messageExtra
import com.example.digileaf.notificationID
import com.example.digileaf.titleExtra
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar

object NotificationHelper {
    fun createNotificationChannel(
        context: Context,
        channelId: String,
        channelName: String,
        channelDescription: String,
        importance: Int
    ) {
        val notificationChannel = NotificationChannel(channelId, channelName, importance)
        notificationChannel.description = channelDescription

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    fun scheduleNotification(context: Context, reminder: Reminder) {
        if (reminder.dueDate() == null && reminder.dueTime() == null) {
            return
        }

        // defaults for date and time if not set
        val dueDate = reminder.dueDate() ?: LocalDate.now()
        val dueTime = reminder.dueTime() ?: LocalTime.of(9, 0)

        val intent = Intent(context, Notification::class.java)
        val title = reminder.title
        val message = reminder.desc

        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val alarmIntent = PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime(dueDate, dueTime)

        if (reminder.repetitionType == RepetitionType.NEVER) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                alarmIntent
            )
        } else {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                time,
                reminder.repetitionType.interval,
                alarmIntent
            )
        }
    }

    fun cancelNotification(context: Context, notificationId: Int) {
        // Implement the logic to cancel the scheduled notification with the specified notificationId
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // get previous alarmIntent by reminder id
        val alarmIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            Intent(context, Notification::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(
            alarmIntent
        )
    }

    fun sendNotification(context: Context, title: String, description: String, icon: Int){
        val intent = Intent(context, Notification::class.java)

        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, description)

        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)
    }

    private fun getTime(dueDate: LocalDate, dueTime: LocalTime): Long {
        val minute = dueTime.minute
        val hour = dueTime.hour
        val day = dueDate.dayOfMonth
        val month = dueDate.monthValue - 1
        val year = dueDate.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }
}
