package com.shypotato.tracker.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

/**
 * This is currently common for both entity and domain.
 * If in the future, we require different sets then split this please
 * */
@Entity(tableName = "food")
data class FoodEntity(
    @ColumnInfo("name") val name: String,
    @ColumnInfo("single_serving_size") val singleServingSizeInGm: Int,
    @ColumnInfo("fibre_per_micro_gram") val fibrePerMicroGram: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    val fibrePerGram: BigDecimal
        get() = BigDecimal.valueOf(fibrePerMicroGram / 1_000_000.0)
}
