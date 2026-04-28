package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.data.helpers.convertFoodEntityToEntryEntity
import com.mayurdw.fibretracker.model.entity.FoodEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IAddEntryUseCase {
    suspend operator fun invoke(foodEntity: FoodEntity)
}

class AddEntryUseCase @Inject constructor(
    private val entryDao: AppDao,
    private val dispatcher: CoroutineDispatcher
) : IAddEntryUseCase {
    override suspend fun invoke(foodEntity: FoodEntity) {
        withContext(dispatcher) {
            entryDao.upsertEntry(convertFoodEntityToEntryEntity(foodEntity))
        }
    }
}
