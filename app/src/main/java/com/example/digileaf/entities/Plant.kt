package com.example.digileaf.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="plants")
data class Plant (
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "species") val species: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image_path") val imagePath: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)