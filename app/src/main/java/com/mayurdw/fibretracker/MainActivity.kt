package com.mayurdw.fibretracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mayurdw.fibretracker.ui.destinations.AddAmountItem
import com.mayurdw.fibretracker.ui.destinations.AddFoodItem
import com.mayurdw.fibretracker.ui.destinations.AddFoodItemScreen
import com.mayurdw.fibretracker.ui.destinations.AddNewFoodItem
import com.mayurdw.fibretracker.ui.destinations.AddNewFoodScreen
import com.mayurdw.fibretracker.ui.destinations.BowelQuality
import com.mayurdw.fibretracker.ui.destinations.Chart
import com.mayurdw.fibretracker.ui.destinations.ChartScreen
import com.mayurdw.fibretracker.ui.destinations.ChooseEntry
import com.mayurdw.fibretracker.ui.destinations.ConfirmPoopQuality
import com.mayurdw.fibretracker.ui.destinations.EditEntry
import com.mayurdw.fibretracker.ui.destinations.EditFoodEntryScreen
import com.mayurdw.fibretracker.ui.destinations.EditMenu
import com.mayurdw.fibretracker.ui.destinations.EnterEditedFood
import com.mayurdw.fibretracker.ui.destinations.FoodQuantityScreen
import com.mayurdw.fibretracker.ui.destinations.Home
import com.mayurdw.fibretracker.ui.destinations.HomeScreen
import com.mayurdw.fibretracker.ui.destinations.Plan
import com.mayurdw.fibretracker.ui.destinations.PlanScreen
import com.mayurdw.fibretracker.ui.destinations.SelectFoodToEdit
import com.mayurdw.fibretracker.ui.destinations.Setting
import com.mayurdw.fibretracker.ui.destinations.SettingsScreen
import com.mayurdw.fibretracker.ui.destinations.getDestination
import com.mayurdw.fibretracker.ui.screens.ChooseEntryScreen
import com.mayurdw.fibretracker.ui.screens.ConfirmBowelQualityScreen
import com.mayurdw.fibretracker.ui.screens.EditMenuScreen
import com.mayurdw.fibretracker.ui.screens.FibreTrackerTopBar
import com.mayurdw.fibretracker.ui.screens.PoopQualityScreen
import com.mayurdw.fibretracker.ui.screens.core.BottomBar
import com.mayurdw.fibretracker.ui.theme.FibreTrackerTheme
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

                            EditFoodEntryScreen(selectedFoodId = foodItem.selectedEntryId) {
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
                            PoopQualityScreen {
                                navController.navigate(ConfirmPoopQuality(it))
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
