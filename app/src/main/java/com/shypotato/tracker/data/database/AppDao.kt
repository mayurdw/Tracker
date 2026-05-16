package com.shypotato.tracker.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.shypotato.tracker.model.entity.EntryEntity
import com.shypotato.tracker.model.entity.EntryEntityId
import com.shypotato.tracker.model.entity.FoodEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface AppDao {
    /**
     * Food related methods
     * */
    @Query("SELECT * FROM food")
    fun getAllFoods(): Flow<List<FoodEntity>>

    @Upsert
    suspend fun upsertNewFood(foodEntity: FoodEntity)

    @Query("SELECT * FROM food WHERE id LIKE :id")
    suspend fun getFoodById(id: Int): FoodEntity?

    @Delete
    suspend fun deleteFood(foodEntity: FoodEntity)

    /**
     * Entry Related Info
     * */

    @Query(
        "SELECT * from trackingEntry " +
                "WHERE date BETWEEN :startDate AND :endDate " +
                "ORDER BY DATE desc"
    )
    fun getEntries(startDate: LocalDate, endDate: LocalDate): Flow<List<EntryEntity>>

    @Query(
        "SELECT * from trackingEntry " +
                "WHERE id LIKE :id "
    )
    suspend fun getEntry(id: Int): EntryEntity

    @Delete(entity = EntryEntity::class)
    suspend fun deleteEntry(vararg id: EntryEntityId)

    @Upsert
    suspend fun upsertEntry(entryEntity: EntryEntity)

}
