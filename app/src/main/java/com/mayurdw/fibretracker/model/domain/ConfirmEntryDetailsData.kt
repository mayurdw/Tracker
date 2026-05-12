package com.mayurdw.fibretracker.model.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class ConfirmEntryDetailsData(
    val title: String,
    val time: LocalTime,
    val date: LocalDate,
    val showDateDialog: Boolean,
    val showTimeDialog: Boolean,
    val canDelete: Boolean,
    val submitEnabled: Boolean
)