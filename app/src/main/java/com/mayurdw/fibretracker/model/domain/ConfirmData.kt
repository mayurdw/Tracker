package com.mayurdw.fibretracker.model.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class ConfirmData(
    val time: LocalTime,
    val date: LocalDate,
    val showTimeDialog: Boolean,
    val showDateDialog: Boolean,
    val canDelete: Boolean,
    val submitEnabled: Boolean,
    val type: ConfirmDataType
)