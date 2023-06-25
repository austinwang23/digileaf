package com.example.digileaf.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName="plants")
@Parcelize
data class Plant (
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "species") val species: String,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "image_path") val imagePath: String? = "",
    @ColumnInfo(name = "last_water") val lastWater: String = "",
    @ColumnInfo(name = "last_fertilize") val lastFertilize: String = "",
    @ColumnInfo(name = "last_groom") val lastGroom: String = "",
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable

data class PlantStatus (
    val id: Int,
    @ColumnInfo(name = "last_water") var lastWater: String,
    @ColumnInfo(name = "last_fertilize") var lastFertilize: String,
    @ColumnInfo(name = "last_groom") var lastGroom: String
)
