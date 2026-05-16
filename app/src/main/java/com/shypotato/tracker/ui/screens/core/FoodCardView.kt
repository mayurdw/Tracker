package com.shypotato.tracker.ui.screens.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun FoodCardView(onCardSelect: () -> Unit = {}, cardContent: @Composable () -> Unit) {
    Card(
        onClick = onCardSelect,
        colors = CardDefaults.elevatedCardColors().copy(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        cardContent()
    }
}
