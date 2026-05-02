package com.mayurdw.fibretracker.model.domain

data class HomeData(
    val hasNext: Boolean,
    val dateData: DateData
) {
    data class DateData(
        val formattedDate: String,
        val listItem: List<ListItem>,
        val fibreOfTheDay: String
    )
}

sealed interface ListItem {
    val itemId: Int

    data class FoodListItem(
        val id: Int,
        val foodName: String,
        val foodQuantity: String,
        val fibreThisMeal: String,
        override val itemId: Int
    ) : ListItem

    data class PoopListItem(
        val id: Int,
        val quality: BowelQuality,
        val time: String,
        override val itemId: Int,
    ) : ListItem
}