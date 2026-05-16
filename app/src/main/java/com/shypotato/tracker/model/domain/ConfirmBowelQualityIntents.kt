package com.shypotato.tracker.model.domain

sealed interface ConfirmBowelQualityIntents {
    data class HandleNewType(val type: BowelType) : ConfirmBowelQualityIntents
}