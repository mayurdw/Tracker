package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.model.entity.PoopEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IAddBowelMovementEntryUseCase {
    suspend operator fun invoke(poopEntity: PoopEntity)
}

class AddBowelMovementEntryUseCase @Inject constructor(
    private val dao: AppDao,
    private val dispatcher: CoroutineDispatcher
) : IAddBowelMovementEntryUseCase {
    override suspend operator fun invoke(poopEntity: PoopEntity) {
        withContext(dispatcher) {
            dao.upsertNewPoop(poopEntity)
        }
    }
}