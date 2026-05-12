package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.data.helpers.convertFoodEntryEntityIntoEntry
import com.mayurdw.fibretracker.model.domain.Entry
import com.mayurdw.fibretracker.model.entity.EntityType.FOOD
import javax.inject.Inject

interface IGetFoodEntryUseCase {
    suspend operator fun invoke(entryId: Int): Entry
}

class GetFoodEntryUseCase @Inject constructor(
    private val entryDao: AppDao,
) : IGetFoodEntryUseCase {
    override suspend operator fun invoke(entryId: Int): Entry {
        val entity = entryDao.getEntry(FOOD, entryId)

        return convertFoodEntryEntityIntoEntry(
            foodEntry = entity,
            foodEntity = entryDao.getFoodById(entity.foodId!!)!!
        )
    }
}

