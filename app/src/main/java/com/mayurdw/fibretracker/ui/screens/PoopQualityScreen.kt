package com.mayurdw.fibretracker.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.mayurdw.fibretracker.model.domain.PoopType
import com.mayurdw.fibretracker.ui.theme.FibreTrackerTheme


@Composable
fun PoopQualityScreen(
    onQualitySelected: (quality: PoopType) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        columns = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)

    ) {
        items(PoopType.entries.toTypedArray()) {
            Card(
                colors = CardDefaults.elevatedCardColors(
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                onClick = { onQualitySelected(it) }
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .width(172.dp)
                        .height(96.dp),
                    painter = painterResource(it.image),
                    contentDescription = null
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onPrimaryContainer)
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    text = it.title
                )

                Text(
                    modifier = Modifier.padding(all = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    text = it.desc,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewPoopQualityScreen() {
    FibreTrackerTheme {
        PoopQualityScreen {

        }
    }
}