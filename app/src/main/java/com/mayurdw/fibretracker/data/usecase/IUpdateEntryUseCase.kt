package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.model.domain.Entry
import com.mayurdw.fibretracker.model.domain.EntryType.Bowel
import com.mayurdw.fibretracker.model.domain.EntryType.Food
import com.mayurdw.fibretracker.model.entity.EntityType.BOWEL_MOVEMENT
import com.mayurdw.fibretracker.model.entity.EntityType.FOOD
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IUpdateEntryUseCase {
    suspend operator fun invoke(entry: Entry)
}

class UpdateEntryUseCase @Inject constructor(
    private val entryDao: AppDao,
    private val dispatcher: CoroutineDispatcher
) : IUpdateEntryUseCase {
    override suspend fun invoke(entry: Entry) {
        withContext(dispatcher) {
            val entryEntity = entryDao.getEntry(
                type = when (entry.info) {
                    is Food -> FOOD
                    is Bowel -> BOWEL_MOVEMENT
                },
                id = entry.id
            )

            val newEntity =
                when (entry.info) {
                    is Food -> {
                        entryEntity.copy(
                            date = entry.date,
                            time = entry.time,
                            foodServingInGms = entry.info.servingInGms
                        ).apply {
                            id = entryEntity.id
                        }
                    }

                    is Bowel -> {
                        entryEntity.copy(
                            date = entry.date,
                            time = entry.time,
                            quality = entry.info.quality
                        ).apply {
                            id = entryEntity.id
                        }
                    }
                }
            entryDao.upsertEntry(newEntity)
        }
    }

}