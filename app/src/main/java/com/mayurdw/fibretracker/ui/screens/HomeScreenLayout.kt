package com.mayurdw.fibretracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.mayurdw.fibretracker.model.domain.HomeData
import com.mayurdw.fibretracker.model.domain.HomeData.DateData
import com.mayurdw.fibretracker.model.domain.HomeData.FoodListItem
import com.mayurdw.fibretracker.model.domain.PoopType
import com.mayurdw.fibretracker.ui.screens.core.FoodCardView
import com.mayurdw.fibretracker.ui.theme.FibreTrackerTheme

@Composable
fun HomeScreenLayout(
    modifier: Modifier,
    homeData: HomeData,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onCardSelected: (id: Int) -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Top
    ) {
        DatePicker(
            modifier = modifier,
            hasNext = homeData.hasNext,
            formattedDate = homeData.dateData.formattedDate,
            onNextClicked = onNextClicked,
            onPreviousClicked = onPreviousClicked
        )

        Spacer(
            modifier
                .fillMaxWidth()
                .height(48.dp)
        )

        FibreValue(modifier, homeData.dateData.fibreOfTheDay)

        Spacer(
            modifier
                .height(48.dp)
        )

        PoopItems(
            modifier,
            homeData.dateData.poopList
        ) { }


        Spacer(modifier = Modifier.height(16.dp))

        FoodItems(
            modifier,
            homeData.dateData.foodItems
        ) {
            onCardSelected(it)
        }

    }
}

@Composable
private fun FibreValue(
    modifier: Modifier,
    value: String,
    units: String = "gm"
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = modifier,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface,
            text = value
        )

        Spacer(modifier.size(8.dp))

        Text(
            modifier = modifier,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.8f),
            text = units
        )
    }
}

@Composable
private fun DatePicker(
    modifier: Modifier,
    hasNext: Boolean,
    formattedDate: String,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = modifier,
            onClick = onPreviousClicked, enabled = true
        ) {
            Icon(
                modifier = modifier,
                imageVector = Filled.ChevronLeft,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            modifier = modifier,
            text = formattedDate,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        IconButton(onClick = onNextClicked, enabled = hasNext) {
            Icon(
                modifier = modifier,
                imageVector = Filled.ChevronRight,
                contentDescription = null,
                tint = if (hasNext) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.inverseOnSurface
            )
        }
    }
}


@Composable
fun PoopItems(
    modifier: Modifier = Modifier,
    poopItems: List<HomeData.PoopListItem>,
    onCardSelected: (id: Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = poopItems, key = { item: HomeData.PoopListItem -> item.id }) {
            FoodCardView(
                onCardSelect = { onCardSelected(it.id) }
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = "Bowel movement"
                    )

                    Text(
                        it.quality.title,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

@Composable
fun FoodItems(
    modifier: Modifier = Modifier,
    foodItems: List<FoodListItem>,
    onCardSelected: (id: Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = foodItems, key = { item: FoodListItem -> item.id }) {
            FoodCardView(
                onCardSelect = { onCardSelected(it.id) }
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        it.foodName,
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    Column(
                        modifier = modifier.heightIn(min = 48.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${it.foodQuantity} gm",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            "${it.fibreThisMeal} gm",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    }
                }
            }
        }
    }
}


@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun PreviewHomeScreenLayout(
    @PreviewParameter(HomeScreenPreviewProvider::class) data: HomeData
) {
    FibreTrackerTheme {
        HomeScreenLayout(
            modifier = Modifier,
            homeData = data,
            onNextClicked = {},
            onPreviousClicked = {}
        )
    }
}

internal class HomeScreenPreviewProvider : PreviewParameterProvider<HomeData> {
    override val values: Sequence<HomeData> = sequenceOf(
        HomeData(
            hasNext = true,
            dateData = DateData(
                "29/5/25",
                fibreOfTheDay = "0.8",
                foodItems = listOf(
                    FoodListItem(
                        id = 1,
                        foodQuantity = "34.9",
                        foodName = "Potato",
                        fibreThisMeal = "0.3"
                    ),
                    FoodListItem(
                        id = 2,
                        foodQuantity = "15.23",
                        foodName = "Chia",
                        fibreThisMeal = "0.5"
                    )
                ),
                poopList = listOf(
                    HomeData.PoopListItem(id = 1, quality = PoopType.TYPE_3)
                )
            )
        ),
        HomeData(
            hasNext = false,
            dateData = DateData(
                formattedDate = "30/5/25",
                fibreOfTheDay = "0.0",
                foodItems = emptyList(),
                poopList = listOf(
                    HomeData.PoopListItem(id = 1, quality = PoopType.TYPE_4)
                )
            )
        )
    )
}
