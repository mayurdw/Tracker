package com.mayurdw.fibretracker.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Entity(tableName = "poop")
data class PoopEntity(
    @ColumnInfo("quality") val quality: Int,
    @ColumnInfo("date") val date: LocalDate,
    @ColumnInfo("time") val time: LocalTime
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
