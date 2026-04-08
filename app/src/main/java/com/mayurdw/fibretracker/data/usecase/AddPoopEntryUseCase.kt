package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.model.entity.PoopEntity
import javax.inject.Inject

class AddPoopEntryUseCase @Inject constructor(
    private val dao: AppDao
) : IAddPoopEntryUseCase {
    override suspend fun addPoopEntry(poopEntity: PoopEntity) {
        dao.upsertNewPoop(poopEntity)
    }
}