package com.shypotato.tracker.ui.screens

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
import com.shypotato.tracker.R
import com.shypotato.tracker.data.helpers.getCurrentDate
import com.shypotato.tracker.data.helpers.getCurrentTime
import com.shypotato.tracker.data.helpers.getFormattedTime
import com.shypotato.tracker.model.domain.BowelType
import com.shypotato.tracker.model.domain.Entry
import com.shypotato.tracker.model.domain.EntryType
import com.shypotato.tracker.model.domain.HomeData
import com.shypotato.tracker.model.domain.HomeData.DateData
import com.shypotato.tracker.ui.screens.core.FoodCardView
import com.shypotato.tracker.ui.theme.FibreTrackerTheme

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
    foodItems: List<Entry>,
    onCardSelected: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = foodItems, key = { item: Entry -> item.id }) { item ->
            when (item.info) {
                is EntryType.Food -> {
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
                                item.info.name,
                                style = MaterialTheme.typography.bodyLarge,
                            )

                            Column(
                                modifier = modifier.heightIn(min = 48.dp),
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.var_gm, item.info.servingInGms),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = stringResource(
                                        R.string.var_gm,
                                        item.info.fibreConsumedInGms.toString()
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.inverseSurface
                                )
                            }
                        }
                    }
                }

                is EntryType.Bowel -> {
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
                                    text = item.info.quality.title,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = item.time.getFormattedTime(),
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
                fibreOfTheDay = "0.0",
                listItem = emptyList()
            )
        ),
        HomeData(
            hasNext = false,
            dateData = DateData(
                formattedDate = "30/5/25",
                fibreOfTheDay = "0.8",
                listItem = listOf(
                    Entry(
                        id = 1,
                        time = getCurrentTime(),
                        date = getCurrentDate(),
                        info = EntryType.Food(
                            name = "Chia",
                            servingInGms = 28,
                            fibrePerMicroGrams = 10
                        )
                    ),
                    Entry(
                        id = 2,
                        time = getCurrentTime(),
                        date = getCurrentDate(),
                        info = EntryType.Bowel(
                            quality = BowelType.TYPE_3
                        )
                    )
                )
            )
        )
    )
}
