package com.example.digileaf.entities

enum class RepetitionType(val text: String, val interval: Long) {
    NEVER("Never",0L),
    HOURLY("Hourly",60 * 60 * 1000L),            // 1 hour
    DAILY("Daily",24 * 60 * 60 * 1000L),         // 24 hours
    WEEKLY("Weekly",7 * 24 * 60 * 60 * 1000L),     // 7 days
    MONTHLY("Monthly",30 * 24 * 60 * 60 * 1000L),   // Approximately 30 days
    YEARLY("Yearly",365 * 24 * 60 * 60 * 1000L),    // Approximately 365 days
}