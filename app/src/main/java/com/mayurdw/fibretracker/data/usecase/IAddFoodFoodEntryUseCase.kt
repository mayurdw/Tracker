package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.model.entity.EntityType
import com.mayurdw.fibretracker.model.entity.EntryEntity
import com.mayurdw.fibretracker.model.entity.FoodEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import javax.inject.Inject

interface IAddFoodEntryUseCase {
    suspend operator fun invoke(foodEntity: FoodEntity, time: LocalTime, date: LocalDate)
}

class AddFoodFoodEntryUseCase @Inject constructor(
    private val entryDao: AppDao,
    private val dispatcher: CoroutineDispatcher
) : IAddFoodEntryUseCase {
    override suspend fun invoke(foodEntity: FoodEntity, time: LocalTime, date: LocalDate) {
        withContext(dispatcher) {
            entryDao.upsertEntry(
                EntryEntity(
                    type = EntityType.FOOD,
                    time = time,
                    date = date,
                    foodId = foodEntity.id,
                    foodServingInGms = foodEntity.singleServingSizeInGm
                )
            )
        }
    }
}
