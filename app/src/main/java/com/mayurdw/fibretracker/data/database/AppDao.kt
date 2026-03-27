package com.mayurdw.fibretracker.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.mayurdw.fibretracker.model.domain.EntryData
import com.mayurdw.fibretracker.model.entity.FoodEntity
import com.mayurdw.fibretracker.model.entity.FoodEntryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface AppDao {
    /**
     * ENTRY related methods
     * */

    @Query(
        "SELECT entry.date AS date, " +
                "entry.serving AS servingInGms, " +
                "entry.foodId AS foodId, " +
                "food.name AS name, " +
                "entry.id AS id," +
                "fibre_per_micro_gram AS fibrePerMicroGrams " +
                "FROM entry, food " +
                "WHERE entry.foodId = food.id " +
                "AND entry.date BETWEEN :startTime AND :endTime " +
                "ORDER BY date DESC"
    )
    fun getEntryData(startTime: LocalDate, endTime: LocalDate): Flow<List<EntryData>>

    @Query(
        "SELECT entry.date AS date, " +
                "entry.serving AS servingInGms, " +
                "entry.foodId AS foodId, " +
                "food.name AS name, " +
                "entry.id AS id," +
                "fibre_per_micro_gram AS fibrePerMicroGrams " +
                "FROM entry, food " +
                "WHERE entry.foodId = food.id " +
                "AND entry.id = :entryId"
    )
    fun getEntry(entryId: Int): Flow<EntryData>

    @Upsert
    fun upsertEntry(entryEntity: FoodEntryEntity)

    @Delete
    suspend fun deleteEntry(foodEntryEntity: FoodEntryEntity)

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
}
