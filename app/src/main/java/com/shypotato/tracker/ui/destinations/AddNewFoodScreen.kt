package com.shypotato.tracker.ui.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shypotato.tracker.R
import com.shypotato.tracker.model.domain.UIState
import com.shypotato.tracker.model.entity.FoodEntity
import com.shypotato.tracker.ui.screens.AddNewFoodScreenLayout
import com.shypotato.tracker.ui.screens.core.LoadingScreen
import com.shypotato.tracker.viewmodels.AddNewFoodViewModel

@Composable
fun AddNewFoodScreen(
    modifier: Modifier = Modifier,
    viewModel: AddNewFoodViewModel = hiltViewModel(),
    selectedFoodId: Int? = null,
    onActionCompleted: () -> Unit = { }
) {
    selectedFoodId?.let {
        val state by viewModel.uiState.collectAsStateWithLifecycle(Lifecycle.State.RESUMED)

        LaunchedEffect(viewModel) {
            viewModel.getFoodById(it)
        }

        when (state) {
            is UIState.Loading -> LoadingScreen()
            is UIState.Success<*> -> {
                val data = (state as UIState.Success<FoodEntity>).data

                AddNewFoodScreenLayout(
                    modifier = modifier,
                    data = data,
                    primaryCtaTextId = R.string.update,
                    buttonIsEnabled = { foodName, foodServing, fibrePerServing ->
                        viewModel.isUpdated(data, foodName, foodServing, fibrePerServing)

                    },
                    onDelete = {
                        viewModel.delete(data)
                        onActionCompleted()
                    }
                ) { foodName: String, foodServing: String, fibrePerServing: String ->
                    viewModel.updateFood(data, foodName, foodServing, fibrePerServing)
                    onActionCompleted()
                }

            }
        }
    } ?: run {
        AddNewFoodScreenLayout(
            modifier = modifier,
            buttonIsEnabled = { foodName, foodServing, fibrePerServing ->
                viewModel.isValid(foodName, foodServing, fibrePerServing)
            },
            onDelete = {}
        ) { foodName: String, foodServing: String, fibrePerServing: String ->
            viewModel.addNewFood(foodName, foodServing, fibrePerServing)
            onActionCompleted()
        }

    }
}
