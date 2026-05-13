package com.mayurdw.fibretracker.ui.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mayurdw.fibretracker.model.domain.BowelType
import com.mayurdw.fibretracker.model.domain.ConfirmBowelQualityIntents.HandleNewType
import com.mayurdw.fibretracker.model.domain.ConfirmData
import com.mayurdw.fibretracker.model.domain.UIState.Loading
import com.mayurdw.fibretracker.model.domain.UIState.Success
import com.mayurdw.fibretracker.ui.screens.ConfirmBowelQualityScreenLayout
import com.mayurdw.fibretracker.ui.screens.core.LoadingScreen
import com.mayurdw.fibretracker.viewmodels.ConfirmBowelQualityViewModel

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
        viewModel.handleIntent(HandleNewType(type))
    }

    if (saveSuccessful) {
        onSaveSuccessful()
    }

    when (uiState) {
        is Loading -> LoadingScreen()
        is Success -> {
            val poopData: ConfirmData =
                (uiState as Success<*>).data as ConfirmData

            ConfirmBowelQualityScreenLayout(
                uiData = poopData,
                onTypeClicked = onTypeClicked,
                onUserEvent = { viewModel.onUserEvent(it) }
            )
        }

        else -> {}
    }

}
