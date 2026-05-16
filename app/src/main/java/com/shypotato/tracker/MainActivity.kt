package com.shypotato.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.shypotato.tracker.ui.destinations.AddAmountItem
import com.shypotato.tracker.ui.destinations.AddFoodItem
import com.shypotato.tracker.ui.destinations.AddFoodItemScreen
import com.shypotato.tracker.ui.destinations.AddNewFoodItem
import com.shypotato.tracker.ui.destinations.AddNewFoodScreen
import com.shypotato.tracker.ui.destinations.BowelQuality
import com.shypotato.tracker.ui.destinations.BowelQualityScreen
import com.shypotato.tracker.ui.destinations.Chart
import com.shypotato.tracker.ui.destinations.ChartScreen
import com.shypotato.tracker.ui.destinations.ChooseEntry
import com.shypotato.tracker.ui.destinations.ChooseEntryScreen
import com.shypotato.tracker.ui.destinations.ConfirmBowelQualityScreen
import com.shypotato.tracker.ui.destinations.ConfirmPoopQuality
import com.shypotato.tracker.ui.destinations.EditEntry
import com.shypotato.tracker.ui.destinations.EditEntryScreen
import com.shypotato.tracker.ui.destinations.EditMenu
import com.shypotato.tracker.ui.destinations.EditMenuScreen
import com.shypotato.tracker.ui.destinations.EnterEditedFood
import com.shypotato.tracker.ui.destinations.FoodQuantityScreen
import com.shypotato.tracker.ui.destinations.Home
import com.shypotato.tracker.ui.destinations.HomeScreen
import com.shypotato.tracker.ui.destinations.Plan
import com.shypotato.tracker.ui.destinations.PlanScreen
import com.shypotato.tracker.ui.destinations.SelectFoodToEdit
import com.shypotato.tracker.ui.destinations.Setting
import com.shypotato.tracker.ui.destinations.SettingsScreen
import com.shypotato.tracker.ui.destinations.getDestination
import com.shypotato.tracker.ui.screens.FibreTrackerTopBar
import com.shypotato.tracker.ui.screens.core.BottomBar
import com.shypotato.tracker.ui.theme.FibreTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val currentDestinationRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route
            val destination = getDestination(currentDestinationRoute)

            FibreTrackerTheme {
                Scaffold(
                    topBar = {
                        FibreTrackerTopBar(
                            currentDestination = destination,
                            onBackPressed = {
                                navController.navigateUp()
                            },
                            settingsActionPressed = {
                                navController.navigate(Setting)
                            })
                    },
                    bottomBar = {
                        if (destination.bottomBarVisible) {
                            BottomBar {
                                navController.navigate(it)
                            }
                        }
                    },
                    floatingActionButton = {
                        if (Home == destination) {
                            IconButton(
                                onClick = {
                                    navController.navigate(ChooseEntry)
                                },
                                modifier = Modifier
                                    .navigationBarsPadding()
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
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
                    },
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = Home,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Home> {
                            HomeScreen {
                                navController.navigate(EditEntry(it))
                            }
                        }
                        composable<AddFoodItem> {
                            AddFoodItemScreen {
                                navController.navigate(AddAmountItem(it.id))
                            }
                        }

                        composable<AddNewFoodItem> {
                            AddNewFoodScreen {
                                navController.navigateUp()
                            }
                        }

                        composable<AddAmountItem> { backStackEntry ->
                            val foodItem: AddAmountItem = backStackEntry.toRoute()
                            FoodQuantityScreen(selectedFood = foodItem.selectedFoodId) {
                                navController.popBackStack(Home, false)
                            }
                        }

                        composable<EditMenu> {
                            EditMenuScreen(
                                onAddClicked = { navController.navigate(AddNewFoodItem) },
                                onEditClicked = { navController.navigate(SelectFoodToEdit) }
                            )
                        }

                        composable<EditEntry> { backStackEntry ->
                            val foodItem: EditEntry = backStackEntry.toRoute()

                            EditEntryScreen(
                                selectedFoodId = foodItem.selectedEntryId,
                            ) {
                                navController.popBackStack(Home, false)
                            }
                        }

                        composable<SelectFoodToEdit> {
                            AddFoodItemScreen {
                                navController.navigate(EnterEditedFood(it.id))
                            }
                        }

                        composable<EnterEditedFood> { backStackEntry ->
                            val foodId: EnterEditedFood = backStackEntry.toRoute()

                            AddNewFoodScreen(selectedFoodId = foodId.selectedFoodId) {
                                navController.navigateUp()
                            }
                        }

                        composable<Plan> {
                            PlanScreen()
                        }

                        composable<Chart> {
                            ChartScreen()
                        }

                        composable<Setting> {
                            SettingsScreen()
                        }

                        composable<ChooseEntry> {
                            ChooseEntryScreen {
                                navController.navigate(it)
                            }
                        }

                        composable<BowelQuality> {
                            BowelQualityScreen { qualitySelected ->
                                navController.navigate(
                                    ConfirmPoopQuality(qualitySelected)
                                )
                            }
                        }

                        composable<ConfirmPoopQuality> {
                            val type: ConfirmPoopQuality = it.toRoute()

                            ConfirmBowelQualityScreen(
                                type = type.quality,
                                onTypeClicked = { navController.navigateUp() },
                                onSaveSuccessful = { navController.popBackStack(Home, false) }
                            )
                        }
                    }
                }
            }
        }
    }
}
