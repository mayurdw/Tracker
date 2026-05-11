package com.mayurdw.fibretracker.ui.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mayurdw.fibretracker.ui.screens.FoodQuantityScreenLayout
import com.mayurdw.fibretracker.ui.screens.core.LoadingScreen
import com.mayurdw.fibretracker.viewmodels.FoodQuantityData
import com.mayurdw.fibretracker.viewmodels.FoodQuantityViewModel
import com.mayurdw.fibretracker.viewmodels.UIState.Error
import com.mayurdw.fibretracker.viewmodels.UIState.Loading
import com.mayurdw.fibretracker.viewmodels.UIState.Success

@Composable
fun FoodQuantityScreen(
    modifier: Modifier = Modifier,
    selectedFood: Int,
    viewModel: FoodQuantityViewModel = hiltViewModel(),
    saveSuccessful: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val savedState by viewModel.entryState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.loadFoodDetails(selectedFood)
    }

    when (savedState) {
        true -> saveSuccessful()
        else -> {}
    }

    when (state) {
        is Success<*> -> {
            val uiState = (state as Success<FoodQuantityData>).data

            FoodQuantityScreenLayout(
                modifier,
                uiData = uiState,
                quantityUpdated = { viewModel.handleQuantityChange(it) },
            ) { detailsIntent ->
                viewModel.onUserEvent(detailsIntent)
            }
        }

        is Loading -> {
            LoadingScreen()
        }

        is Error -> {}


    }
}
