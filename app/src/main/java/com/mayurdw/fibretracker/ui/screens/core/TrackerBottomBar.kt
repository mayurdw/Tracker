package com.mayurdw.fibretracker.ui.screens.core

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Upcoming
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.mayurdw.fibretracker.ui.destinations.Destinations
import com.mayurdw.fibretracker.ui.theme.FibreTrackerTheme

data class TrackerBottomBarButton(
    val label: String,
    val icon: ImageVector,
    val isSelected: Boolean,
    val navRoute: Destinations
)

@PreviewLightDark
@Composable
fun TrackerBottomBar() {
    FibreTrackerTheme {
        Box {
            BottomNavPanel()
        }
    }
}

@Composable
private fun BottomBarIcon(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 8.dp)
            .clickable(
                enabled = !isSelected,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
        )

        Text(
            style = MaterialTheme.typography.labelMedium,
            text = label,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
private fun BoxScope.BottomNavPanelWithCutOut() {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(64.dp)
            .clip(
                BottomNavShape(
                    cornerRadius = with(LocalDensity.current) { 20.dp.toPx() },
                    dockRadius = with(LocalDensity.current) { 38.dp.toPx() },
                ),
            ) // Apply the custom shape
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BottomBarIcon(
                    icon = Filled.Home,
                    label = "Home",
                    isSelected = true
                )

                BottomBarIcon(
                    icon = Filled.Upcoming,
                    label = "Plan",
                    isSelected = false
                )
            }

            Spacer(modifier = Modifier.fillMaxWidth(0.35f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BottomBarIcon(
                    icon = Filled.FoodBank,
                    label = "Edit",
                    isSelected = false
                )

                BottomBarIcon(
                    icon = Filled.BarChart,
                    label = "Relation",
                    isSelected = false
                )
            }
        }


    }
}

@Composable
private fun BoxScope.BottomNavPanel(

) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
    ) {
        BottomNavPanelWithCutOut() // The navigation panel

        // Floating button positioned over the cutout
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .size(58.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Filled.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

class BottomNavShape(
    private val cornerRadius: Float, // Rounded corners at the panel's top
    private val dockRadius: Float,   // Size of the cutout
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {

        // baseRect: Creates a rounded rectangle covering the entire size, with rounded top corners. This is the basic shape of the bottom navigation.
        val baseRect = Path().apply {
            addRoundRect(
                RoundRect(
                    Rect(Offset.Zero, Offset(size.width, size.height)),
                    topLeft = CornerRadius(cornerRadius, cornerRadius),
                    topRight = CornerRadius(cornerRadius, cornerRadius),
                ),
            )
        }

        // rect1: Creates a rectangle from the left edge to just before the center "dock", filling the full height.  Will be cut later to add a corner radius
        val rect1 = Path().apply {
            addRoundRect(
                RoundRect(
                    Rect(Offset.Zero, Offset(size.width / 2 - dockRadius + 4f, size.height)),
                    topLeft = CornerRadius(cornerRadius, cornerRadius),
                ),
            )
        }

        // rect1A: Same rectangle as rect1, but has a smaller topLeft Radius to create a small difference
        val rect1A = Path().apply {
            addRoundRect(
                RoundRect(
                    Rect(Offset.Zero, Offset(size.width / 2 - dockRadius + 4f, size.height)),
                    topLeft = CornerRadius(cornerRadius, cornerRadius),
                    topRight = CornerRadius(32f, 32f),
                ),
            )
        }

        // rect1B: This calculates the area between rect1 and rect1A and is one of the path that cuts into baseRect.
        val rect1B = Path.combine(
            operation = PathOperation.Difference,
            path1 = rect1,
            path2 = rect1A,
        )

        // rect2: Creates a rectangle from just after the center "dock" to the right edge, filling the full height.
        val rect2 = Path().apply {
            addRoundRect(
                RoundRect(
                    Rect(
                        Offset(size.width / 2 + dockRadius - 4f, 0f),
                        Offset(size.width, size.height)
                    ),
                    topRight = CornerRadius(cornerRadius, cornerRadius),
                ),
            )
        }

        // rect2A: Same rectangle as rect2, but has a smaller topRight Radius to create a small difference
        val rect2A = Path().apply {
            addRoundRect(
                RoundRect(
                    Rect(
                        Offset(size.width / 2 + dockRadius - 4f, 0f),
                        Offset(size.width, size.height)
                    ),
                    topRight = CornerRadius(cornerRadius, cornerRadius),
                    topLeft = CornerRadius(32f, 32f),
                ),
            )
        }

        // rect2B: This calculates the area between rect2 and rect2A and is one of the path that cuts into baseRect.
        val rect2B = Path.combine(
            operation = PathOperation.Difference,
            path1 = rect2,
            path2 = rect2A,
        )

        // circle: Creates a circle at the center, just above the bottom navigation, creating the "dock" for the floating action button.
        val circle = Path().apply {
            addOval(
                Rect(
                    Offset(size.width / 2 - dockRadius, -dockRadius),
                    Offset(size.width / 2 + dockRadius, dockRadius),
                ),
            )
        }

        // path1: Subtracts the "circle" path from the base rectangle, creating the main shape with the cut-out for the dock.
        val path1 = Path.combine(
            operation = PathOperation.Difference,
            path1 = baseRect,
            path2 = circle,
        )

        // path2: Subtracts the "rect1B" from the base rectangle, create more curvature on the sides
        val path2 = Path.combine(
            operation = PathOperation.Difference,
            path1 = path1,
            path2 = rect1B,
        )

        // path: Subtracts the "rect2B" from the base rectangle, create more curvature on the sides
        val path = Path.combine(
            operation = PathOperation.Difference,
            path1 = path2,
            path2 = rect2B,
        )

        // Return the final path
        return Outline.Generic(path)
    }
}