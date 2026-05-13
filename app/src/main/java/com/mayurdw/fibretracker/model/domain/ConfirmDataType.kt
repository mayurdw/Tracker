package com.mayurdw.fibretracker.model.domain

import com.mayurdw.fibretracker.model.entity.FoodEntity

sealed interface ConfirmDataType {
    data class Bowel(
        val type: BowelType
    ) : ConfirmDataType

    data class Food(
        val entity: FoodEntity,
        val foodQuantity: String = ""
    ) : ConfirmDataType
}