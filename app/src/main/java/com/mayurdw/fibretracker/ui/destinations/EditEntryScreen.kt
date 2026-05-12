package com.mayurdw.fibretracker.ui.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mayurdw.fibretracker.model.domain.FoodQuantityData
import com.mayurdw.fibretracker.model.domain.UIState.Error
import com.mayurdw.fibretracker.model.domain.UIState.Loading
import com.mayurdw.fibretracker.model.domain.UIState.Success
import com.mayurdw.fibretracker.ui.screens.FoodQuantityScreenLayout
import com.mayurdw.fibretracker.ui.screens.core.LoadingScreen
import com.mayurdw.fibretracker.viewmodels.EditFoodEntryViewModel

@Composable
fun EditEntryScreen(
    modifier: Modifier = Modifier,
    selectedFoodId: Int,
    viewModel: EditFoodEntryViewModel = hiltViewModel<EditFoodEntryViewModel>(),
    onTypeSelected: () -> Unit,
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
