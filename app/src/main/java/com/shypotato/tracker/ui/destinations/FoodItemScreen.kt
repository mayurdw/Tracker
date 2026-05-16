package com.shypotato.tracker.ui.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.State.RESUMED
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shypotato.tracker.model.entity.FoodEntity
import com.shypotato.tracker.ui.screens.AddFoodItemList
import com.shypotato.tracker.ui.screens.core.LoadingScreen
import com.shypotato.tracker.viewmodels.AddFoodEntryViewModel
import com.shypotato.tracker.model.domain.UIState


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
        is UIState.Success<*> -> {
            val data = entries as UIState.Success<List<FoodEntity>>

            AddFoodItemList(
                foodItems = data.data
            ) {
                onItemSelect(it)
            }
        }

        is UIState.Loading -> {
            LoadingScreen()
        }

        is UIState.Error -> {}

    }
}
