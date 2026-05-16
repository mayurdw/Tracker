package com.shypotato.tracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shypotato.tracker.model.domain.CommonFoods
import com.shypotato.tracker.model.entity.FoodEntity
import com.shypotato.tracker.ui.screens.core.FoodCardView
import com.shypotato.tracker.ui.theme.FibreTrackerTheme

@Composable
fun AddFoodItemList(
    modifier: Modifier = Modifier,
    foodItems: List<FoodEntity>,
    onItemSelect: (FoodEntity) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        itemsIndexed(
            items = foodItems,
            key = { index: Int, foodItem: FoodEntity -> foodItem.id }
        ) { _: Int, foodItem: FoodEntity ->
            FoodItem(modifier, foodItem, onItemSelect)
        }
    }
}

@Composable
fun FoodItem(
    modifier: Modifier = Modifier,
    foodItem: FoodEntity,
    onItemSelect: (FoodEntity) -> Unit
) {
    FoodCardView(
        onCardSelect = { onItemSelect(foodItem) }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = foodItem.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier
                    .fillMaxWidth()
                    .sizeIn(minHeight = 32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddFoodItemListPreview() {
    FibreTrackerTheme {
        AddFoodItemList(
            foodItems = CommonFoods,
            onItemSelect = {}
        )
    }
}
