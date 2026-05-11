package com.mayurdw.fibretracker.viewmodels

import com.mayurdw.fibretracker.model.domain.BowelType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class ConfirmBowelQualityData(
    val type: BowelType,
    val time: LocalTime,
    val date: LocalDate,
    val showTimeDialog: Boolean,
    val showDateDialog: Boolean
)