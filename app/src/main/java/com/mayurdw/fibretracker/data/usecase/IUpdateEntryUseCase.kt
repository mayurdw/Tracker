package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.model.entity.FoodEntryEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IUpdateEntryUseCase {
    suspend operator fun invoke(foodEntryEntity: FoodEntryEntity)
}

class UpdateEntryUseCase @Inject constructor(
    private val entryDao: AppDao,
    private val dispatcher: CoroutineDispatcher
) : IUpdateEntryUseCase {
    override suspend fun invoke(foodEntryEntity: FoodEntryEntity) {
        withContext(dispatcher) {
            entryDao.upsertEntry(foodEntryEntity)
        }
    }

}