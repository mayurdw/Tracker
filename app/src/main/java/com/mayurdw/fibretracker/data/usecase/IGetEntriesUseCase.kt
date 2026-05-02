package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.data.helpers.convertBowelEntryEntityIntoEntry
import com.mayurdw.fibretracker.data.helpers.convertFoodEntryEntityIntoEntry
import com.mayurdw.fibretracker.model.domain.Entry
import com.mayurdw.fibretracker.model.entity.EntityType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import javax.inject.Inject

interface IGetEntriesUseCase {
    suspend operator fun invoke(startDate: LocalDate, endDate: LocalDate): Flow<List<Entry>>
}

class GetEntriesUseCase @Inject constructor(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val dao: AppDao
) : IGetEntriesUseCase {
    override suspend fun invoke(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Entry>> {
        return channelFlow {
            withContext(coroutineDispatcher) {
                dao.getEntries(startDate, endDate).map { entities ->
                    entities.map { entity ->
                        when (entity.type) {
                            EntityType.FOOD -> {
                                convertFoodEntryEntityIntoEntry(
                                    foodEntry = entity,
                                    foodEntity = dao.getFoodById(entity.foodId!!)!!
                                )
                            }

                            EntityType.BOWEL_MOVEMENT -> {
                                convertBowelEntryEntityIntoEntry(bowelEntity = entity)
                            }
                        }
                    }
                }.collectLatest { entries ->
                    trySend(entries)
                }
            }
        }
    }
}