package com.mayurdw.fibretracker.model.domain

import com.mayurdw.fibretracker.model.entity.FoodEntity
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class FoodQuantityData(
    val entity: FoodEntity,
    val time: LocalTime,
    val date: LocalDate,
    val showDateDialog: Boolean,
    val showTimeDialog: Boolean,
    val submitEnabled: Boolean,
    val canDelete: Boolean,
    val foodQuantity: String = ""
)