package com.mayurdw.fibretracker.model.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class ConfirmBowelQualityData(
    val type: BowelType,
    val time: LocalTime,
    val date: LocalDate,
    val showTimeDialog: Boolean,
    val showDateDialog: Boolean
)