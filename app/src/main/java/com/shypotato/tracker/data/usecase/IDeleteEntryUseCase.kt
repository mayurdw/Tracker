package com.shypotato.tracker.data.usecase

import com.shypotato.tracker.data.database.AppDao
import com.shypotato.tracker.model.domain.Entry
import com.shypotato.tracker.model.entity.EntryEntityId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IDeleteEntryUseCase {
    suspend operator fun invoke(entity: Entry)
}

class DeleteEntryUseCase @Inject constructor(
    private val appDao: AppDao,
    private val dispatcher: CoroutineDispatcher
) : IDeleteEntryUseCase {
    override suspend operator fun invoke(entity: Entry) {
        withContext(dispatcher) {
            appDao.deleteEntry(
                EntryEntityId(entity.id)
            )
        }
    }
}
