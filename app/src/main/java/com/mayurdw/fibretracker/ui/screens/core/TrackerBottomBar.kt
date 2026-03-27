package com.mayurdw.fibretracker.ui.screens.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.mayurdw.fibretracker.ui.destinations.AddFoodItem
import com.mayurdw.fibretracker.ui.destinations.Chart
import com.mayurdw.fibretracker.ui.destinations.ChooseEntry
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

    Box {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .navigationBarsPadding()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
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
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomBarIcon(
                    isSelected = 2 == navNum,
                    selectedIcon = Icons.Filled.Upcoming,
                    unselectedIcon = Icons.Outlined.Upcoming,
                    contentDescription = "Message"
                ) {
                    navigationDestination(Plan)
                    navNum = 2
                }

                BottomBarIcon(
                    isSelected = 3 == navNum,
                    selectedIcon = Icons.Filled.BarChart,
                    unselectedIcon = Icons.Outlined.BarChart,
                    contentDescription = "Message"
                ) {
                    navigationDestination(Chart)
                    navNum = 3
                }
            }
        }

        IconButton(
            onClick = {
                navigationDestination(ChooseEntry)
            },
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 35.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .align(Alignment.BottomCenter)
                .padding(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "add",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}