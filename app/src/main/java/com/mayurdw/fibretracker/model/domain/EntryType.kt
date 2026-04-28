package com.mayurdw.fibretracker.model.domain

import java.math.BigDecimal

sealed interface EntryType {
    data class Food(
        val name: String,
        val servingInGms: Int,
        val fibrePerMicroGrams: Int
    ) : EntryType {
        val fibreConsumedInGms: BigDecimal =
            BigDecimal.valueOf(servingInGms * fibrePerMicroGrams / 1_000_000.00)
    }

    data class Poop(val quality: PoopType) : EntryType
}