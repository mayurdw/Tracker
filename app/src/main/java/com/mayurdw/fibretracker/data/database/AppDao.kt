package com.mayurdw.fibretracker.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.mayurdw.fibretracker.model.domain.EntryData
import com.mayurdw.fibretracker.model.domain.FoodEntryData
import com.mayurdw.fibretracker.model.entity.EntryEntity
import com.mayurdw.fibretracker.model.entity.EntryEntityId
import com.mayurdw.fibretracker.model.entity.FoodEntity
import com.mayurdw.fibretracker.model.entity.FoodEntryEntity
import com.mayurdw.fibretracker.model.entity.PoopEntity
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
    fun getEntryData(startTime: LocalDate, endTime: LocalDate): Flow<List<FoodEntryData>>

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


    @Upsert
    suspend fun upsertNewPoop(poopEntity: PoopEntity)

    @Query("SELECT * from poop WHERE poop.date BETWEEN :startDate AND :endDate")
    fun getPoopEntries(startDate: LocalDate, endDate: LocalDate): Flow<List<PoopEntity>>

    @Query(
        "SELECT * from trackingEntry " +
                "WHERE date BETWEEN :startDate AND :endDate " +
                "ORDER BY DATE desc"
    )
    fun getEntries(startDate: LocalDate, endDate: LocalDate): Flow<List<EntryEntity>>

    @Delete(entity = EntryEntity::class)
    suspend fun deleteEntry(vararg id: EntryEntityId)

    @Upsert
    suspend fun upsertEntry(entryEntity: EntryEntity)

}
