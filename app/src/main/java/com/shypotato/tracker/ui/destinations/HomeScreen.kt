package com.shypotato.tracker.ui.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shypotato.tracker.model.domain.HomeData
import com.shypotato.tracker.ui.screens.HomeScreenLayout
import com.shypotato.tracker.ui.screens.core.LoadingScreen
import com.shypotato.tracker.viewmodels.HomeScreenViewModel
import com.shypotato.tracker.model.domain.UIState


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel<HomeScreenViewModel>(),
    onCardSelected: (id: Int) -> Unit
) {
    val homeState by viewModel.homeStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.getLatestData()
    }

    when (homeState) {
        is UIState.Success<*> -> {
            val homeData = homeState as UIState.Success<HomeData>
            HomeScreenLayout(
                modifier = modifier,
                homeData = homeData.data,
                onNextClicked = { viewModel.onDateChanged(false) },
                onPreviousClicked = { viewModel.onDateChanged(true) }) {
                onCardSelected(it)
            }
        }

        is UIState.Loading -> {
            LoadingScreen(modifier)
        }

        is UIState.Error -> {

        }
    }
}
