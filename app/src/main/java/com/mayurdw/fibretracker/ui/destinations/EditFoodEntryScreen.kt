package com.mayurdw.fibretracker.ui.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mayurdw.fibretracker.ui.screens.FoodQuantityScreenLayout
import com.mayurdw.fibretracker.ui.screens.core.LoadingScreen
import com.mayurdw.fibretracker.viewmodels.EditFoodEntryViewModel
import com.mayurdw.fibretracker.viewmodels.FoodQuantityData
import com.mayurdw.fibretracker.viewmodels.UIState.Error
import com.mayurdw.fibretracker.viewmodels.UIState.Loading
import com.mayurdw.fibretracker.viewmodels.UIState.Success

@Composable
fun EditFoodEntryScreen(
    modifier: Modifier = Modifier,
    selectedFoodId: Int,
    viewModel: EditFoodEntryViewModel = hiltViewModel<EditFoodEntryViewModel>(),
    saveSuccessful: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val saveState by viewModel.saveSuccessful.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getEntryData(selectedFoodId)
    }

    when (saveState) {
        true -> saveSuccessful()
        else -> {}
    }

    when (state) {
        is Loading -> {
            LoadingScreen()
        }

        is Success<*> -> {
            val entry = (state as Success<FoodQuantityData>).data

            FoodQuantityScreenLayout(
                modifier = modifier,
                quantityUpdated = { viewModel.isEdited(it) },
                uiData = entry,
            ) { detailsIntent ->
                viewModel.onUserEvent(detailsIntent)
            }

        }

        is Error -> {}
    }
}
