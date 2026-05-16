package com.shypotato.tracker.ui.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shypotato.tracker.model.domain.ConfirmData
import com.shypotato.tracker.ui.screens.FoodQuantityScreenLayout
import com.shypotato.tracker.ui.screens.core.LoadingScreen
import com.shypotato.tracker.viewmodels.FoodQuantityViewModel
import com.shypotato.tracker.model.domain.UIState

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
        is UIState.Success<*> -> {
            val uiState = (state as UIState.Success<ConfirmData>).data

            FoodQuantityScreenLayout(
                modifier,
                uiData = uiState,
                quantityUpdated = { viewModel.handleQuantityChange(it) },
            ) { detailsIntent ->
                viewModel.onUserEvent(detailsIntent)
            }
        }

        is UIState.Loading -> {
            LoadingScreen()
        }

        is UIState.Error -> {}


    }
}
