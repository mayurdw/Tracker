package com.mayurdw.fibretracker.ui.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.State.RESUMED
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mayurdw.fibretracker.model.entity.FoodEntity
import com.mayurdw.fibretracker.ui.screens.AddFoodItemList
import com.mayurdw.fibretracker.ui.screens.core.LoadingScreen
import com.mayurdw.fibretracker.viewmodels.AddFoodEntryViewModel
import com.mayurdw.fibretracker.viewmodels.UIState.Error
import com.mayurdw.fibretracker.viewmodels.UIState.Loading
import com.mayurdw.fibretracker.viewmodels.UIState.Success


@Composable
fun AddFoodItemScreen(
    viewModel: AddFoodEntryViewModel = hiltViewModel(),
    onItemSelect: (food: FoodEntity) -> Unit
) {
    val entries by viewModel.entryState.collectAsStateWithLifecycle(minActiveState = RESUMED)

    LaunchedEffect(viewModel) {
        viewModel.loadData()
    }

    when (entries) {
        is Success<*> -> {
            val data = entries as Success<List<FoodEntity>>

            AddFoodItemList(
                foodItems = data.data
            ) {
                onItemSelect(it)
            }
        }

        is Loading -> {
            LoadingScreen()
        }

        is Error -> {}

    }
}
