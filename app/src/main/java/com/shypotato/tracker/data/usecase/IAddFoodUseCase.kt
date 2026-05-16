package com.shypotato.tracker.data.usecase

import com.shypotato.tracker.data.database.AppDao
import com.shypotato.tracker.model.entity.FoodEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IAddFoodUseCase {
    suspend operator fun invoke(newFoodItem: FoodEntity)
}

class AddFoodUseCase @Inject constructor(
    private val dao: AppDao,
    private val dispatcher: CoroutineDispatcher
) : IAddFoodUseCase {

    override suspend operator fun invoke(newFoodItem: FoodEntity) {
        withContext(dispatcher) {
            dao.upsertNewFood(foodEntity = newFoodItem)
        }
    }
}
