package com.shypotato.tracker.data.usecase

import android.content.res.Resources
import com.shypotato.tracker.data.database.AppDao
import com.shypotato.tracker.model.entity.FoodEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IGetFoodUseCase {
    suspend operator fun invoke(id: Int): Flow<Result<FoodEntity>>
}

class GetFoodUseCase @Inject constructor(
    private val dao: AppDao,
    private val dispatcher: CoroutineDispatcher
) : IGetFoodUseCase {
    override suspend operator fun invoke(id: Int): Flow<Result<FoodEntity>> {
        return channelFlow {
            withContext(dispatcher) {
                dao.getFoodById(id)?.let {
                    trySend(Result.success(it))
                } ?: run {
                    trySend(Result.failure(Resources.NotFoundException()))
                }
            }
        }
    }
}
