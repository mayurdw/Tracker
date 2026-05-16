package com.mayurdw.fibretracker.ui.screens.core

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SetMeal
import androidx.compose.material.icons.filled.Upcoming
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.SetMeal
import androidx.compose.material.icons.outlined.Upcoming
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.mayurdw.fibretracker.ui.destinations.Chart
import com.mayurdw.fibretracker.ui.destinations.Destinations
import com.mayurdw.fibretracker.ui.destinations.EditMenu
import com.mayurdw.fibretracker.ui.destinations.Home
import com.mayurdw.fibretracker.ui.destinations.Plan
import com.mayurdw.fibretracker.ui.theme.FibreTrackerTheme

@PreviewLightDark
@Composable
private fun PreviewBottomBar() {
    FibreTrackerTheme {
        BottomBar { _ ->

        }
    }
}

@Composable
private fun BottomBarIcon(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier.sizeIn(48.dp, 48.dp),
        enabled = !isSelected,
        onClick = onClick
    ) {
        Icon(
            imageVector = if (isSelected) selectedIcon else unselectedIcon,
            contentDescription = contentDescription,
            tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimaryFixed,
            modifier = Modifier
                .size(25.dp)
        )
    }
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navigationDestination: (destination: Destinations) -> Unit
) {
    var navNum by remember {
        mutableIntStateOf(0)
    }

    AnimatedNavigationBar(
        modifier = modifier.navigationBarsPadding(),
        ballColor = MaterialTheme.colorScheme.primary,
        barColor = MaterialTheme.colorScheme.primaryContainer,
        selectedIndex = navNum
    ) {
        BottomBarIcon(
            isSelected = 0 == navNum,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            contentDescription = "home"
        ) {
            navigationDestination(Home)
            navNum = 0
        }

        BottomBarIcon(
            isSelected = 1 == navNum,
            selectedIcon = Icons.Filled.SetMeal,
            unselectedIcon = Icons.Outlined.SetMeal,
            contentDescription = "calendar"
        ) {
            navigationDestination(EditMenu)
            navNum = 1
        }
//
//        BottomBarIcon(
//            isSelected = 2 == navNum,
//            selectedIcon = Icons.Filled.Upcoming,
//            unselectedIcon = Icons.Outlined.Upcoming,
//            contentDescription = "Message"
//        ) {
//            navigationDestination(Plan)
//            navNum = 2
//        }

        BottomBarIcon(
            isSelected = 2 == navNum,
            selectedIcon = Icons.Filled.BarChart,
            unselectedIcon = Icons.Outlined.BarChart,
            contentDescription = "Message"
        ) {
            navigationDestination(Chart)
            navNum = 2
        }
    }
}