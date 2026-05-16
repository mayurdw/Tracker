package com.shypotato.tracker.data

import android.content.Context
import androidx.room.Room
import com.shypotato.tracker.data.database.AppDao
import com.shypotato.tracker.data.database.AppDatabase
import com.shypotato.tracker.data.usecase.AddBowelMovementEntryUseCase
import com.shypotato.tracker.data.usecase.AddFoodFoodEntryUseCase
import com.shypotato.tracker.data.usecase.AddFoodUseCase
import com.shypotato.tracker.data.usecase.DeleteEntryUseCase
import com.shypotato.tracker.data.usecase.DeleteFoodUseCase
import com.shypotato.tracker.data.usecase.GetAllFoodsUseCase
import com.shypotato.tracker.data.usecase.GetEntriesUseCase
import com.shypotato.tracker.data.usecase.GetEntryUseCase
import com.shypotato.tracker.data.usecase.GetFoodUseCase
import com.shypotato.tracker.data.usecase.IAddBowelMovementEntryUseCase
import com.shypotato.tracker.data.usecase.IAddFoodEntryUseCase
import com.shypotato.tracker.data.usecase.IAddFoodUseCase
import com.shypotato.tracker.data.usecase.IDeleteEntryUseCase
import com.shypotato.tracker.data.usecase.IDeleteFoodUseCase
import com.shypotato.tracker.data.usecase.IGetAllFoodsUseCase
import com.shypotato.tracker.data.usecase.IGetEntriesUseCase
import com.shypotato.tracker.data.usecase.IGetEntryUseCase
import com.shypotato.tracker.data.usecase.IGetFoodUseCase
import com.shypotato.tracker.data.usecase.IUpdateEntryUseCase
import com.shypotato.tracker.data.usecase.UpdateEntryUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                "app-database"
            )
            .build()
    }

    @Provides
    fun getFoodEntryDao(database: AppDatabase): AppDao {
        return database.getAppDao()
    }

    @Provides
    fun providerDispatcher(): CoroutineDispatcher {
        return IO
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun provideFoodUseCase(addFoodUseCase: AddFoodUseCase): IAddFoodUseCase

    @Binds
    abstract fun provideAddEntryUseCase(addFoodEntryUseCase: AddFoodFoodEntryUseCase): IAddFoodEntryUseCase

    @Binds
    abstract fun provideUpdateEntryUseCase(updateEntryUseCase: UpdateEntryUseCase): IUpdateEntryUseCase

    @Binds
    abstract fun provideGetEntryUseCase(getFoodEntryUseCase: GetEntryUseCase): IGetEntryUseCase

    @Binds
    abstract fun provideGetAllFoodsUseCase(getAllFoodsUseCase: GetAllFoodsUseCase): IGetAllFoodsUseCase

    @Binds
    abstract fun provideGetFoodUseCase(getFoodUseCase: GetFoodUseCase): IGetFoodUseCase

    @Binds
    abstract fun provideDeleteFoodUseCase(deleteFoodUseCase: DeleteFoodUseCase): IDeleteFoodUseCase

    @Binds
    abstract fun provideDeleteEntryUseCase(deleteEntryUseCase: DeleteEntryUseCase): IDeleteEntryUseCase

    @Binds
    abstract fun provideAddPoopEntryUseCase(addPoopEntryUseCase: AddBowelMovementEntryUseCase): IAddBowelMovementEntryUseCase

    @Binds
    abstract fun provideGetEntriesUseCase(getEntriesUseCase: GetEntriesUseCase): IGetEntriesUseCase
}
