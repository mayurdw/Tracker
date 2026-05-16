package com.shypotato.tracker.data.usecase

import com.shypotato.tracker.data.database.AppDao
import com.shypotato.tracker.data.helpers.convertBowelEntryEntityIntoEntry
import com.shypotato.tracker.data.helpers.convertFoodEntryEntityIntoEntry
import com.shypotato.tracker.model.domain.Entry
import com.shypotato.tracker.model.entity.EntityType
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
            EntityType.FOOD -> convertFoodEntryEntityIntoEntry(
                foodEntry = entity,
                foodEntity = entryDao.getFoodById(entity.foodId!!)!!
            )

            EntityType.BOWEL_MOVEMENT -> convertBowelEntryEntityIntoEntry(
                bowelEntity = entity
            )
        }
    }
}

