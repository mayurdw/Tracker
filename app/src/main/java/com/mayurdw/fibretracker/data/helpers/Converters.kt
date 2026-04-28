package com.mayurdw.fibretracker.data.helpers

import com.mayurdw.fibretracker.model.domain.FoodEntryDatas
import com.mayurdw.fibretracker.model.domain.ListItem
import com.mayurdw.fibretracker.model.entity.FoodEntity
import com.mayurdw.fibretracker.model.entity.FoodEntryEntity
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun convertFoodEntityToEntryEntity(foodEntity: FoodEntity): FoodEntryEntity {
    val date : LocalDate = getDateToday()

    return FoodEntryEntity(
        foodId = foodEntity.id,
        foodServingInGms = foodEntity.singleServingSizeInGm,
        date = date
    )
}

fun convertFoodEntryEntityToFoodListItem(
    entryData: FoodEntryDatas,
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
