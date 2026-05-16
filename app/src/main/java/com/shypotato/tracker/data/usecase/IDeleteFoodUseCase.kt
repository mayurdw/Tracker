package com.shypotato.tracker.data.usecase

import com.shypotato.tracker.data.database.AppDao
import com.shypotato.tracker.model.entity.FoodEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IDeleteFoodUseCase {
    suspend operator fun invoke(foodEntity: FoodEntity)
}

class DeleteFoodUseCase @Inject constructor(
    private val appDao: AppDao,
    private val dispatcher: CoroutineDispatcher
) : IDeleteFoodUseCase {
    override suspend operator fun invoke(foodEntity: FoodEntity) {
        withContext(dispatcher) {
            appDao.deleteFood(foodEntity)
        }
    }
}
