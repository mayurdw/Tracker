package com.mayurdw.fibretracker.ui.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mayurdw.fibretracker.model.domain.HomeData
import com.mayurdw.fibretracker.ui.screens.HomeScreenLayout
import com.mayurdw.fibretracker.ui.screens.core.LoadingScreen
import com.mayurdw.fibretracker.viewmodels.HomeScreenViewModel
import com.mayurdw.fibretracker.viewmodels.UIState.Error
import com.mayurdw.fibretracker.viewmodels.UIState.Loading
import com.mayurdw.fibretracker.viewmodels.UIState.Success


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
        is Success<*> -> {
            val homeData = homeState as Success<HomeData>
            HomeScreenLayout(
                modifier = modifier,
                homeData = homeData.data,
                onNextClicked = { viewModel.onDateChanged(false) },
                onPreviousClicked = { viewModel.onDateChanged(true) }) {
                onCardSelected(it)
            }
        }

        is Loading -> {
            LoadingScreen(modifier)
        }

        is Error -> {

        }
    }
}
