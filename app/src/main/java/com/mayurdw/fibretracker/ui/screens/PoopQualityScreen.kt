package com.mayurdw.fibretracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.mayurdw.fibretracker.ui.theme.FibreTrackerTheme


@Composable
fun PoopQualityScreen() {
    val listOfItems: List<String> = listOf(
        "Separate Hard Lumps, like little pebbles (Hard to pass)",
        "Hard and lumpy and starting to resemble a sausage",
        "Sausage-shaped with cracks on the surface",
        "Thinner and more snakelike, plus smooth and soft",
        "Soft blobs with clear cut edges",
        "Fluffy, mushy pieces with ragged edges",
        "Watery with no solid pieces"
    )

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        columns = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)

    ) {
        items(listOfItems) {
            Card(
                colors = CardDefaults.elevatedCardColors(
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    modifier = Modifier.padding(all = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    text = it,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewPoopQualityScreen() {
    FibreTrackerTheme {
        PoopQualityScreen()
    }
}