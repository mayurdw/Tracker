package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.model.domain.FoodEntryData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import javax.inject.Inject


interface IGetFoodEntriesUseCase {
    suspend operator fun invoke(currentDate: LocalDate): Flow<List<FoodEntryData>>
}

class GetFoodEntriesUseCase @Inject constructor(
    private val entryDao: AppDao,
    private val dispatcher: CoroutineDispatcher
) : IGetFoodEntriesUseCase {
    override suspend operator fun invoke(currentDate: LocalDate): Flow<List<FoodEntryData>> =
        channelFlow {
            withContext(dispatcher) {
                entryDao.getEntryData(currentDate, currentDate).collectLatest {
                    trySend(it)
                }
            }
        }
}