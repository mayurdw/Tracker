package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.model.entity.PoopEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface IGetPoopEntryUseCase {
    suspend fun getPoopEntries(startDate: LocalDate, endDate: LocalDate): Flow<List<PoopEntity>>
}