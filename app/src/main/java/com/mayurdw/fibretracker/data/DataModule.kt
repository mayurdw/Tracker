package com.mayurdw.fibretracker.data

import android.content.Context
import androidx.room.Room
import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.data.database.AppDatabase
import com.mayurdw.fibretracker.data.usecase.AddEntryUseCase
import com.mayurdw.fibretracker.data.usecase.AddFoodUseCase
import com.mayurdw.fibretracker.data.usecase.AddPoopEntryUseCase
import com.mayurdw.fibretracker.data.usecase.DeleteEntryUseCase
import com.mayurdw.fibretracker.data.usecase.DeleteFoodUseCase
import com.mayurdw.fibretracker.data.usecase.GetEntryUseCase
import com.mayurdw.fibretracker.data.usecase.GetFoodUseCase
import com.mayurdw.fibretracker.data.usecase.GetPoopEntryUseCase
import com.mayurdw.fibretracker.data.usecase.IAddEntryUseCase
import com.mayurdw.fibretracker.data.usecase.IAddFoodUseCase
import com.mayurdw.fibretracker.data.usecase.IAddPoopEntryUseCase
import com.mayurdw.fibretracker.data.usecase.IDeleteEntryUseCase
import com.mayurdw.fibretracker.data.usecase.IDeleteFoodUseCase
import com.mayurdw.fibretracker.data.usecase.IGetEntryUseCase
import com.mayurdw.fibretracker.data.usecase.IGetFoodUseCase
import com.mayurdw.fibretracker.data.usecase.IGetPoopEntryUseCase
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
    abstract fun provideAddEntryUseCase(addEntryUseCase: AddEntryUseCase): IAddEntryUseCase

    @Binds
    abstract fun provideGetEntryUseCase(getEntryUseCase: GetEntryUseCase): IGetEntryUseCase

    @Binds
    abstract fun provideGetFoodUseCase(getFoodUseCase: GetFoodUseCase): IGetFoodUseCase

    @Binds
    abstract fun provideDeleteFoodUseCase(deleteFoodUseCase: DeleteFoodUseCase): IDeleteFoodUseCase

    @Binds
    abstract fun provideDeleteEntryUseCase(deleteEntryUseCase: DeleteEntryUseCase): IDeleteEntryUseCase

    @Binds
    abstract fun provideAddPoopEntryUseCase(addPoopEntryUseCase: AddPoopEntryUseCase): IAddPoopEntryUseCase

    @Binds
    abstract fun provideGetPoopEntryUseCase(getPoopEntryUseCase: GetPoopEntryUseCase): IGetPoopEntryUseCase
}
