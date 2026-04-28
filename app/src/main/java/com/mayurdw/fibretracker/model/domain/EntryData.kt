package com.mayurdw.fibretracker.model.domain

import kotlinx.datetime.LocalDate
import java.math.BigDecimal
import java.math.BigDecimal.valueOf

data class FoodEntryData(
    val id: Int,
    val foodId: Int,
    val name: String,
    val servingInGms: Int,
    val fibrePerMicroGrams: Int
) {
    val fibreConsumedInGms: BigDecimal
        get() = valueOf(servingInGms * fibrePerMicroGrams / 1_000_000.00)
}

data class EntryData(
    val id: Int,
    val foodId: Int,
    val date: LocalDate,
    val name: String,
    val servingInGms: Int,
    val fibrePerMicroGrams: Int
) {
    val fibreConsumedInGms: BigDecimal
        get() = valueOf(servingInGms * fibrePerMicroGrams / 1_000_000.00)
}
