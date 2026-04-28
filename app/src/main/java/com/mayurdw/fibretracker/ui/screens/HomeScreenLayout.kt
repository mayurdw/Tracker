package com.mayurdw.fibretracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.mayurdw.fibretracker.R
import com.mayurdw.fibretracker.model.domain.HomeData
import com.mayurdw.fibretracker.model.domain.HomeData.DateData
import com.mayurdw.fibretracker.model.domain.ListItem
import com.mayurdw.fibretracker.model.domain.ListItem.FoodListItem
import com.mayurdw.fibretracker.model.domain.ListItem.PoopListItem
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

        EntryList(
            modifier,
            homeData.dateData.listItem
        ) {
            onCardSelected(it)
        }

    }
}

@Composable
private fun FibreValue(
    modifier: Modifier,
    value: String,
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
            text = stringResource(R.string.gm)
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
fun EntryList(
    modifier: Modifier = Modifier,
    foodItems: List<ListItem>,
    onCardSelected: (id: Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = foodItems, key = { item: ListItem -> item.itemId }) { item ->
            when (item) {
                is FoodListItem -> {
                    FoodCardView(
                        onCardSelect = { onCardSelected(item.id) }
                    ) {
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                item.foodName,
                                style = MaterialTheme.typography.bodyLarge,
                            )

                            Column(
                                modifier = modifier.heightIn(min = 48.dp),
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.var_gm, item.foodQuantity),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = stringResource(R.string.var_gm, item.fibreThisMeal),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.inverseSurface
                                )
                            }
                        }
                    }
                }

                is PoopListItem -> {
                    FoodCardView(
                        onCardSelect = { onCardSelected(item.id) }
                    ) {
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.bowel_movement),
                                style = MaterialTheme.typography.bodyLarge,
                            )

                            Column(
                                modifier = modifier.heightIn(min = 48.dp),
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = item.quality.title,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = item.time,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.inverseSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@PreviewLightDark
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
                listItem = listOf(
                    FoodListItem(
                        itemId = 1,
                        id = 1,
                        foodQuantity = "34.9",
                        foodName = "Potato",
                        fibreThisMeal = "0.3"
                    ),
                    FoodListItem(
                        itemId = 2,
                        id = 2,
                        foodQuantity = "15.23",
                        foodName = "Chia",
                        fibreThisMeal = "0.5"
                    ),
                    PoopListItem(
                        itemId = 3,
                        id = 1,
                        quality = PoopType.TYPE_3,
                        time = "01.23 pm"
                    )
                )
            )
        ),
        HomeData(
            hasNext = false,
            dateData = DateData(
                formattedDate = "30/5/25",
                fibreOfTheDay = "0.0",
                listItem = emptyList()
            )
        )
    )
}
