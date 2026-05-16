package com.shypotato.tracker.model.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class Entry(
    val id: Int,
    val time: LocalTime,
    val date: LocalDate,
    val info: EntryType
)