package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.model.entity.PoopEntity

interface IAddPoopEntryUseCase {
    suspend fun addPoopEntry(poopEntity: PoopEntity)
}