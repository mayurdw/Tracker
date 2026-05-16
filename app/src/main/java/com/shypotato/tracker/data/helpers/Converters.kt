package com.shypotato.tracker.data.helpers

import com.shypotato.tracker.model.domain.Entry
import com.shypotato.tracker.model.entity.EntryEntity
import com.shypotato.tracker.model.entity.FoodEntity
import com.shypotato.tracker.model.domain.EntryType

fun convertFoodEntryEntityIntoEntry(
    foodEntry: EntryEntity,
    foodEntity: FoodEntity
): Entry {
    return Entry(
        id = foodEntry.id,
        time = foodEntry.time,
        date = foodEntry.date,
        info = EntryType.Food(
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
        info = EntryType.Bowel(
            quality = bowelEntity.quality!!
        )
    )
}