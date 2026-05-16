package com.shypotato.tracker.model.domain

import com.shypotato.tracker.model.entity.FoodEntity

sealed interface ConfirmDataType {
    data class Bowel(
        val type: BowelType
    ) : ConfirmDataType

    data class Food(
        val entity: FoodEntity,
        val foodQuantity: String = ""
    ) : ConfirmDataType
}