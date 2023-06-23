package com.example.digileaf.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(foreignKeys = [ForeignKey(
    entity = Plant::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("plantId"),
    onDelete = ForeignKey.CASCADE
)], tableName="journals")
@Parcelize
data class Journal (
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "entry") val entry: String,
    @ColumnInfo(name = "image_path") val imagePath: String? = null,
    @ColumnInfo(name = "plantId", index=true) val plantId: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable