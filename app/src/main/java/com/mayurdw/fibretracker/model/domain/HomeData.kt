package com.mayurdw.fibretracker.model.domain

data class HomeData(
    val hasNext: Boolean,
    val dateData: DateData
) {
    data class DateData(
        val formattedDate: String,
        val foodItems: List<FoodListItem>,
        val poopList: List<PoopListItem>,
        val fibreOfTheDay: String
    )

    data class PoopListItem(
        val id: Int,
        val quality: PoopType
    )

    data class FoodListItem(
        val id: Int,
        val foodName: String,
        val foodQuantity: String,
        val fibreThisMeal: String
    )
}
