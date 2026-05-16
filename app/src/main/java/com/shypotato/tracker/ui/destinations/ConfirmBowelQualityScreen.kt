package com.shypotato.tracker.ui.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shypotato.tracker.model.domain.BowelType
import com.shypotato.tracker.model.domain.ConfirmData
import com.shypotato.tracker.ui.screens.ConfirmBowelQualityScreenLayout
import com.shypotato.tracker.ui.screens.core.LoadingScreen
import com.shypotato.tracker.viewmodels.ConfirmBowelQualityViewModel
import com.shypotato.tracker.model.domain.ConfirmBowelQualityIntents
import com.shypotato.tracker.model.domain.UIState

@Composable
fun ConfirmBowelQualityScreen(
    type: BowelType,
    viewModel: ConfirmBowelQualityViewModel = hiltViewModel(),
    onTypeClicked: () -> Unit,
    onSaveSuccessful: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val saveSuccessful by viewModel.submissionSuccessful.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(ConfirmBowelQualityIntents.HandleNewType(type))
    }

    if (saveSuccessful) {
        onSaveSuccessful()
    }

    when (uiState) {
        is UIState.Loading -> LoadingScreen()
        is UIState.Success -> {
            val poopData: ConfirmData =
                (uiState as UIState.Success<*>).data as ConfirmData

            ConfirmBowelQualityScreenLayout(
                uiData = poopData,
                onTypeClicked = onTypeClicked,
                onUserEvent = { viewModel.onUserEvent(it) }
            )
        }

        else -> {}
    }

}
