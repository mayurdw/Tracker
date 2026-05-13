package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.data.helpers.convertBowelEntryEntityIntoEntry
import com.mayurdw.fibretracker.data.helpers.convertFoodEntryEntityIntoEntry
import com.mayurdw.fibretracker.model.domain.Entry
import com.mayurdw.fibretracker.model.entity.EntityType.BOWEL_MOVEMENT
import com.mayurdw.fibretracker.model.entity.EntityType.FOOD
import javax.inject.Inject

interface IGetEntryUseCase {
    suspend operator fun invoke(entryId: Int): Entry
}

class GetEntryUseCase @Inject constructor(
    private val entryDao: AppDao,
) : IGetEntryUseCase {
    override suspend operator fun invoke(entryId: Int): Entry {
        val entity = entryDao.getEntry(entryId)

        return when (entity.type) {
            FOOD -> convertFoodEntryEntityIntoEntry(
                foodEntry = entity,
                foodEntity = entryDao.getFoodById(entity.foodId!!)!!
            )

            BOWEL_MOVEMENT -> convertBowelEntryEntityIntoEntry(
                bowelEntity = entity
            )
        }
    }
}

