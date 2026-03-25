package com.mayurdw.fibretracker.model.domain

import com.mayurdw.fibretracker.model.entity.PoopEntity
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import java.math.BigDecimal

data class FoodEntryData(
    val foodId: Int,
    val name: String,
    val servingInGms: Int,
    val fibrePerMicroGrams: Int
) {
    val fibreConsumedInGms: BigDecimal
        get() = BigDecimal.valueOf(servingInGms * fibrePerMicroGrams / 1_000_000.00)
}

sealed interface EntryType {
    data class Food(val food: FoodEntryData) : EntryType
    data class Poop(val poopEntity: PoopEntity) : EntryType
}

data class Entry(
    val id: Int,
    val time: LocalTime,
    val date: LocalDate,
    val info: EntryType
)

data class EntryData(
    val id: Int,
    val foodId: Int,
    val date: LocalDate,
    val name: String,
    val servingInGms: Int,
    val fibrePerMicroGrams: Int
) {
    val fibreConsumedInGms: BigDecimal
        get() = BigDecimal.valueOf(servingInGms * fibrePerMicroGrams / 1_000_000.00)
}
