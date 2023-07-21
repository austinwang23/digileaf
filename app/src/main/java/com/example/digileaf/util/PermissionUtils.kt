package com.example.digileaf.util

import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun isLocationPermissionGranted(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun isNotificationPermissionGranted(context: Context): Boolean {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    return notificationManager.areNotificationsEnabled()
}