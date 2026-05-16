package com.shypotato.tracker.data.usecase

import com.shypotato.tracker.data.database.AppDao
import com.shypotato.tracker.model.domain.BowelType
import com.shypotato.tracker.model.entity.EntryEntity
import com.shypotato.tracker.model.entity.EntityType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import javax.inject.Inject

interface IAddBowelMovementEntryUseCase {
    suspend operator fun invoke(bowelType: BowelType, time: LocalTime, date: LocalDate)
}

class AddBowelMovementEntryUseCase @Inject constructor(
    private val dao: AppDao,
    private val dispatcher: CoroutineDispatcher
) : IAddBowelMovementEntryUseCase {
    override suspend fun invoke(bowelType: BowelType, time: LocalTime, date: LocalDate) {
        withContext(dispatcher) {
            dao.upsertEntry(
                EntryEntity(
                    type = EntityType.BOWEL_MOVEMENT,
                    time = time,
                    date = date,
                    quality = bowelType
                )
            )
        }
    }
}