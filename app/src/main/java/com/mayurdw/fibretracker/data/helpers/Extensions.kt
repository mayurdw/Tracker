package com.mayurdw.fibretracker.data.helpers

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.LocalTime.Companion.Format
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days


fun getDateTimeNow(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}

fun getDateToday(): LocalDate {
    return getDateTimeNow().date
}

fun getCurrentTime(): LocalTime {
    return getDateTimeNow().time
}

fun LocalDate.getFormattedDate(): String {
    val format = LocalDate.Format {
        day()
        char('/')
        monthNumber()
        char('/')
        yearTwoDigits(2000)
    }

    return this.format(format)
}

fun LocalDate.toDateInMilliSecs(): Long {
    return this.toEpochDays().days.inWholeMilliseconds
}

fun LocalTime.getFormattedTime(): String {
    val timeFormat = Format {
        amPmHour()
        char(':')
        minute()
        char(' ')
        amPmMarker("am", "pm")
    }

    return this.format(timeFormat)

}