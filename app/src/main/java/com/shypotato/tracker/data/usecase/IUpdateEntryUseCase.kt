package com.shypotato.tracker.data.usecase

import com.shypotato.tracker.data.database.AppDao
import com.shypotato.tracker.model.domain.Entry
import com.shypotato.tracker.model.domain.EntryType
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
                id = entry.id
            )

            val newEntity =
                when (entry.info) {
                    is EntryType.Food -> {
                        entryEntity.copy(
                            date = entry.date,
                            time = entry.time,
                            foodServingInGms = entry.info.servingInGms
                        ).apply {
                            id = entryEntity.id
                        }
                    }

                    is EntryType.Bowel -> {
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