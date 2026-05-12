package com.mayurdw.fibretracker.data.helpers

import com.mayurdw.fibretracker.model.domain.Entry
import com.mayurdw.fibretracker.model.domain.EntryType.Bowel
import com.mayurdw.fibretracker.model.domain.EntryType.Food
import com.mayurdw.fibretracker.model.entity.EntryEntity
import com.mayurdw.fibretracker.model.entity.FoodEntity

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