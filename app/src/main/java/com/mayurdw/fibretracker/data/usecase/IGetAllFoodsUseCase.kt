package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.model.domain.CommonFoods
import com.mayurdw.fibretracker.model.entity.FoodEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IGetAllFoodsUseCase {
    suspend operator fun invoke(): Flow<List<FoodEntity>>
}

class GetAllFoodsUseCase @Inject constructor(
    private val dao: AppDao,
    private val dispatcher: CoroutineDispatcher
) : IGetAllFoodsUseCase {
    override suspend operator fun invoke(): Flow<List<FoodEntity>> {
        return channelFlow {
            withContext(dispatcher) {
                val foods = dao.getAllFoods().first()

                if (foods.isEmpty()) {
                    CommonFoods.forEach {
                        dao.upsertNewFood(it)
                    }
                    trySend(CommonFoods)
                } else {
                    trySend(foods)
                }
            }
        }
    }
}
