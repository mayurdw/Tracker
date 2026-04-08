package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.model.entity.PoopEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class GetPoopEntryUseCase @Inject constructor(
    private val dao: AppDao
) : IGetPoopEntryUseCase {
    override suspend fun getPoopEntries(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<PoopEntity>> {
        return dao.getPoopEntries(startDate, endDate)
    }

}