package com.mayurdw.fibretracker.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mayurdw.fibretracker.model.domain.BowelQuality
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Entity(tableName = "trackingEntry")
data class EntryEntity(
    // Mandatory regardless of the entry type
    @ColumnInfo("date")
    val date: LocalDate,

    @ColumnInfo("time")
    val time: LocalTime,

    @ColumnInfo("type")
    val type: EntityType,

    // EntityType.FOOD
    @ColumnInfo("foodId")
    val foodId: Int? = null,

    @ColumnInfo("serving")
    val foodServingInGms: Int? = null,

    // EntityType.BOWEL
    @ColumnInfo("quality")
    val quality: BowelQuality? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

data class EntryEntityId(
    val id: Int
)
