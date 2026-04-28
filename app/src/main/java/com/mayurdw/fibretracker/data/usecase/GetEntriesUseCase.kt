package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.model.domain.FoodEntryDatas
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import javax.inject.Inject


interface IGetEntriesUseCase {
    suspend operator fun invoke(currentDate: LocalDate): Flow<List<FoodEntryDatas>>
}

class GetEntriesUseCase @Inject constructor(
    private val entryDao: AppDao,
    private val dispatcher: CoroutineDispatcher
) : IGetEntriesUseCase {
    override suspend operator fun invoke(currentDate: LocalDate): Flow<List<FoodEntryDatas>> =
        channelFlow {
            withContext(dispatcher) {
                entryDao.getEntryData(currentDate, currentDate).collectLatest {
                    trySend(it)
                }
            }
        }
}