package com.shypotato.tracker.data.helpers

import androidx.room.TypeConverter
import kotlinx.datetime.LocalTime

class LocalTimeConverters {

    @TypeConverter
    fun fromLocalTimeToInt(time: LocalTime): Int = time.toSecondOfDay()

    @TypeConverter
    fun toLocalTimeFromInt(secondOfDay: Int): LocalTime = LocalTime.fromSecondOfDay(secondOfDay)
}