package com.mayurdw.fibretracker.data.usecase

import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.model.domain.EntryData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

interface IGetEntryUseCase {
    suspend operator fun invoke(entryId: Int): Flow<EntryData>
}

class GetEntryUseCase @Inject constructor(
    private val entryDao: AppDao,
    private val dispatcher: CoroutineDispatcher
) : IGetEntryUseCase {
    override suspend operator fun invoke(entryId: Int): Flow<EntryData> {
        return channelFlow {
            entryDao.getEntry(entryId).collectLatest {
                trySend(it)
            }
        }
    }
}

