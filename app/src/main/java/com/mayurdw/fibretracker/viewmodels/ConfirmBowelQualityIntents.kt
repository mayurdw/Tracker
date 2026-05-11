package com.mayurdw.fibretracker.viewmodels

import com.mayurdw.fibretracker.model.domain.BowelType

sealed interface ConfirmBowelQualityIntents {
    data class HandleNewType(val type: BowelType) : ConfirmBowelQualityIntents
}