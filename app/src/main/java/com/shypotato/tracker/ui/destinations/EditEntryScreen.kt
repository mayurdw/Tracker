package com.shypotato.tracker.ui.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shypotato.tracker.model.domain.ConfirmData
import com.shypotato.tracker.model.domain.ConfirmDataType
import com.shypotato.tracker.ui.screens.ConfirmBowelQualityScreenLayout
import com.shypotato.tracker.ui.screens.FoodQuantityScreenLayout
import com.shypotato.tracker.ui.screens.core.LoadingScreen
import com.shypotato.tracker.viewmodels.EditFoodEntryViewModel
import com.shypotato.tracker.model.domain.UIState

@Composable
fun EditEntryScreen(
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
        is UIState.Loading -> {
            LoadingScreen()
        }

        is UIState.Success<*> -> {
            val entry = (state as UIState.Success<ConfirmData>).data

            when (entry.type) {
                is ConfirmDataType.Food -> {
                    FoodQuantityScreenLayout(
                        modifier = modifier,
                        quantityUpdated = { viewModel.isEdited(it) },
                        uiData = entry,
                    ) { detailsIntent ->
                        viewModel.onUserEvent(detailsIntent)
                    }
                }

                is ConfirmDataType.Bowel -> {
                    ConfirmBowelQualityScreenLayout(
                        uiData = entry,
                        onTypeClicked = {}
                    ) {
                        viewModel.onUserEvent(it)
                    }
                }
            }

        }

        is UIState.Error -> {}
    }
}
