package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.model.domain.EntryData
import com.mayurdw.fibretracker.model.entity.FoodEntryEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IDeleteEntryUseCase {
    suspend operator fun invoke(entity: EntryData)
}

class DeleteEntryUseCase @Inject constructor(
    private val appDao: AppDao,
    private val dispatcher: CoroutineDispatcher
) : IDeleteEntryUseCase {
    override suspend operator fun invoke(entity: EntryData) {
        withContext(dispatcher) {
            appDao.deleteEntry(
                foodEntryEntity = FoodEntryEntity(
                    foodId = entity.foodId,
                    foodServingInGms = entity.servingInGms,
                    date = entity.date
                ).apply { id = entity.id })
        }
    }
}
