package com.mayurdw.fibretracker.ui.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mayurdw.fibretracker.model.domain.EntryData
import com.mayurdw.fibretracker.ui.screens.FoodQuantityScreenLayout
import com.mayurdw.fibretracker.ui.screens.core.LoadingScreen
import com.mayurdw.fibretracker.viewmodels.EditFoodEntryViewModel
import com.mayurdw.fibretracker.viewmodels.UIState.Error
import com.mayurdw.fibretracker.viewmodels.UIState.Loading
import com.mayurdw.fibretracker.viewmodels.UIState.Success
import java.math.BigDecimal

@Composable
fun EditFoodEntryScreen(
    modifier: Modifier = Modifier,
    selectedFoodId: Int,
    viewModel: EditFoodEntryViewModel = hiltViewModel<EditFoodEntryViewModel>(),
    saveSuccessful: () -> Unit = {}
) {
    val state by viewModel.selectedEntryFlow.collectAsStateWithLifecycle(Lifecycle.State.RESUMED)
    val saveState by viewModel.saveSuccessful.collectAsStateWithLifecycle(Lifecycle.State.RESUMED)

    LaunchedEffect(Unit) {
        viewModel.getEntryData(selectedFoodId)
    }

    when (saveState) {
        true -> saveSuccessful()
    }

    when (state) {
        is Loading -> {
            LoadingScreen()
        }

        is Success<*> -> {
            val entry = (state as Success<*>).data as EntryData

            FoodQuantityScreenLayout(
                modifier = modifier,
                foodName = entry.name,
                fibrePerGram = BigDecimal.valueOf(entry.fibrePerMicroGrams / 1_000_000.0),
                singleServingSizeInGm = entry.servingInGms,
                buttonEnabled = { viewModel.isEdited(it, entry) },
                canDelete = true,
                onDeleteClicked = {
                    viewModel.delete(entry)
                }
            ) {
                viewModel.updateEntry(it, entry)
            }

        }

        is Error -> {}
    }
}
