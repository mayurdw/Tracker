package com.mayurdw.fibretracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bathroom
import androidx.compose.material.icons.filled.SetMeal
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.mayurdw.fibretracker.ui.destinations.AddFoodItem
import com.mayurdw.fibretracker.ui.destinations.Destinations
import com.mayurdw.fibretracker.ui.destinations.Home
import com.mayurdw.fibretracker.ui.destinations.PoopQuality
import com.mayurdw.fibretracker.ui.theme.FibreTrackerTheme


@PreviewLightDark
@Composable
private fun PreviewChooseEntryScreen() {
    FibreTrackerTheme {
        ChooseEntryScreen {

        }
    }
}

@Composable
private fun EntryCard(
    cardText: String,
    icon: ImageVector,
    onEntrySelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        onClick = onEntrySelected,
        colors = CardDefaults.elevatedCardColors(
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = cardText,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
fun ChooseEntryScreen(
    onEntrySelected: (destination: Destinations) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {

        EntryCard(
            cardText = "Record Food Entry",
            icon = Icons.Filled.SetMeal
        ) {
            onEntrySelected(AddFoodItem)
        }

        EntryCard(
            cardText = "Record Bowel Movement",
            icon = Icons.Filled.Bathroom
        ) {
            onEntrySelected(PoopQuality)
        }

    }
}