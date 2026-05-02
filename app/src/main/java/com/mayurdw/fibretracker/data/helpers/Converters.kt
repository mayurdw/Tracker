package com.mayurdw.fibretracker.data.helpers

import com.mayurdw.fibretracker.model.domain.BowelQuality
import com.mayurdw.fibretracker.model.domain.Entry
import com.mayurdw.fibretracker.model.domain.EntryType.Bowel
import com.mayurdw.fibretracker.model.domain.EntryType.Food
import com.mayurdw.fibretracker.model.domain.FoodEntryData
import com.mayurdw.fibretracker.model.domain.ListItem
import com.mayurdw.fibretracker.model.entity.EntityType.BOWEL_MOVEMENT
import com.mayurdw.fibretracker.model.entity.EntryEntity
import com.mayurdw.fibretracker.model.entity.FoodEntity
import com.mayurdw.fibretracker.model.entity.FoodEntryEntity
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

fun convertFoodEntityToEntryEntity(foodEntity: FoodEntity): FoodEntryEntity {
    val date: LocalDate = getDateToday()

    return FoodEntryEntity(
        foodId = foodEntity.id,
        foodServingInGms = foodEntity.singleServingSizeInGm,
        date = date
    )
}

fun convertFoodEntryEntityToFoodListItem(
    entryData: FoodEntryData,
    index: Int
): ListItem.FoodListItem {
    return ListItem.FoodListItem(
        id = entryData.id,
        foodName = entryData.name,
        foodQuantity =
            "${entryData.servingInGms}",
        fibreThisMeal = entryData.fibreConsumedInGms.toString(),
        itemId = index
    )
}

fun convertBowelMovementIntoEntity(
    type: BowelQuality,
    date: LocalDate,
    time: LocalTime
): EntryEntity {
    return EntryEntity(
        date = date,
        time = time,
        type = BOWEL_MOVEMENT,
        quality = type
    )
}

fun convertFoodEntryEntityIntoEntry(
    foodEntry: EntryEntity,
    foodEntity: FoodEntity
): Entry {
    return Entry(
        id = foodEntry.id,
        time = foodEntry.time,
        date = foodEntry.date,
        info = Food(
            name = foodEntity.name,
            servingInGms = foodEntry.foodServingInGms!!,
            fibrePerMicroGrams = foodEntity.fibrePerMicroGram
        )
    )
}

fun convertBowelEntryEntityIntoEntry(
    bowelEntity: EntryEntity
): Entry {
    return Entry(
        id = bowelEntity.id,
        time = bowelEntity.time,
        date = bowelEntity.date,
        info = Bowel(
            quality = bowelEntity.quality!!
        )
    )
}