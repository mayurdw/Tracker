package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.model.entity.PoopEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import javax.inject.Inject

interface IGetBowelMovementEntryUseCase {
    suspend operator fun invoke(
        startDate: LocalDate
    ): Flow<List<PoopEntity>>
}

class GetBowelMovementEntryUseCase @Inject constructor(
    private val dao: AppDao,
) : IGetBowelMovementEntryUseCase {
    override suspend operator fun invoke(
        startDate: LocalDate
    ): Flow<List<PoopEntity>> {
        return dao.getPoopEntries(startDate, startDate)
    }

}