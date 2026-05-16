package com.shypotato.tracker.ui.screens

import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.shypotato.tracker.ui.destinations.AddAmountItem
import com.shypotato.tracker.ui.destinations.AddFoodItem
import com.shypotato.tracker.ui.destinations.AddNewFoodItem
import com.shypotato.tracker.ui.destinations.Destinations
import com.shypotato.tracker.ui.destinations.EditEntry
import com.shypotato.tracker.ui.destinations.EditMenu
import com.shypotato.tracker.ui.destinations.Home
import com.shypotato.tracker.ui.destinations.getTitle
import com.shypotato.tracker.ui.theme.FibreTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FibreTrackerTopBar(
    currentDestination: Destinations = Home,
    onBackPressed: () -> Unit = {},
    settingsActionPressed: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        title = {
            Text(
                stringResource(
                    getTitle(currentDestination)
                )
            )
        },
        navigationIcon = {
            if (currentDestination.backButtonVisible) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Filled.ChevronLeft,
                        contentDescription = null
                    )
                }
            }
        },
        actions = {
//            if (!currentDestination.backButtonVisible) {
//                IconButton(onClick = settingsActionPressed) {
//                    Icon(
//                        imageVector = Icons.Filled.Settings,
//                        contentDescription = null
//                    )
//                }
//            }
        }
    )
}

@Composable
@PreviewLightDark
private fun PreviewFibreTrackerTopBar(
    @PreviewParameter(TopBarPreviewProvider::class) destination: Destinations
) {
    FibreTrackerTheme {
        FibreTrackerTopBar(destination) { }
    }
}

internal class TopBarPreviewProvider : PreviewParameterProvider<Destinations> {
    override val values: Sequence<Destinations> = sequenceOf(
        Home,
        AddFoodItem,
        AddNewFoodItem,
        EditMenu,
        AddAmountItem(-1),
        EditEntry(-1)
    )
}
