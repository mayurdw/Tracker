package com.mayurdw.fibretracker.model.domain

sealed interface ConfirmBowelQualityIntents {
    data class HandleNewType(val type: BowelType) : ConfirmBowelQualityIntents
}