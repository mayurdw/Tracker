package com.shypotato.tracker.data.helpers

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class LocalDateConverters {
    @TypeConverter
    fun fromLocalDateToLong(date: LocalDate): Long = date.toEpochDays()

    @TypeConverter
    fun toLocalDateFromLong(epochDays: Long): LocalDate = LocalDate.fromEpochDays(epochDays)
}
