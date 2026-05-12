package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.model.domain.Entry
import com.mayurdw.fibretracker.model.entity.EntryEntityId
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
