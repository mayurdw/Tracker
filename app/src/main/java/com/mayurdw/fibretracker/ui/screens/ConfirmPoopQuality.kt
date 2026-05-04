package com.mayurdw.fibretracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mayurdw.fibretracker.R
import com.mayurdw.fibretracker.data.helpers.getCurrentTime
import com.mayurdw.fibretracker.data.helpers.getDateToday
import com.mayurdw.fibretracker.model.domain.BowelType
import com.mayurdw.fibretracker.model.domain.BowelType.TYPE_4
import com.mayurdw.fibretracker.ui.screens.core.LoadingScreen
import com.mayurdw.fibretracker.ui.theme.FibreTrackerTheme
import com.mayurdw.fibretracker.viewmodels.ConfirmBowelQualityData
import com.mayurdw.fibretracker.viewmodels.ConfirmBowelQualityViewModel
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleDateDismissed
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleDateOpened
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleNewType
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleSubmission
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleTimeDismissed
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleTimeOpened
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleUpdatedDate
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleUpdatedTime
import com.mayurdw.fibretracker.viewmodels.UIState.Loading
import com.mayurdw.fibretracker.viewmodels.UIState.Success

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
            val poopData: ConfirmBowelQualityData =
                (uiState as Success<*>).data as ConfirmBowelQualityData

            ConfirmBowelQualityScreenLayout(
                uiData = poopData,
                onTimeUpdated = { hour, min ->
                    viewModel.handleIntent(
                        HandleUpdatedTime(
                            hour,
                            min
                        )
                    )
                },
                onTypeClicked = onTypeClicked,
                onDateDialogDismissed = { viewModel.handleIntent(HandleDateDismissed) },
                onDateDialogOpened = { viewModel.handleIntent(HandleDateOpened) },
                onTimeDialogDismissed = { viewModel.handleIntent(HandleTimeDismissed) },
                onTimeDialogOpened = { viewModel.handleIntent(HandleTimeOpened) },
                onDateUpdated = { viewModel.handleIntent(HandleUpdatedDate(it)) },
                onSubmitClicked = { viewModel.handleIntent(HandleSubmission) },
            )
        }

        else -> {}
    }

}

@Composable
fun ConfirmBowelQualityScreenLayout(
    uiData: ConfirmBowelQualityData,
    onDateDialogDismissed: () -> Unit,
    onDateDialogOpened: () -> Unit,
    onTimeDialogDismissed: () -> Unit,
    onTimeDialogOpened: () -> Unit,
    onTimeUpdated: (hour: Int, min: Int) -> Unit,
    onDateUpdated: (newDate: Long?) -> Unit,
    onTypeClicked: () -> Unit,
    onSubmitClicked: () -> Unit,
) {

    ConfirmEntryDetailsScreenLayout(
        headerTitle = R.string.confirm_details,
        time = uiData.time,
        date = uiData.date,
        showDateDialog = uiData.showDateDialog,
        showTimeDialog = uiData.showTimeDialog,
        onDateDialogDismissed = onDateDialogDismissed,
        onDateDialogOpened = onDateDialogOpened,
        onTimeDialogDismissed = onTimeDialogDismissed,
        onTimeDialogOpened = onTimeDialogOpened,
        onTimeUpdated = onTimeUpdated,
        onDateUpdated = onDateUpdated,
        onSubmitClicked = onSubmitClicked,
        entryDetailsLayout = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Bowel Quality",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedCard(
                    modifier = Modifier.heightIn(48.dp),
                    onClick = onTypeClicked,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = uiData.type.title,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        footerDetailsLayout = {

        },
        canDelete = false,
        buttonEnabled = true,
        onDeleteClicked = {}
    )
}


class ConfirmBowelQualityProvider : PreviewParameterProvider<ConfirmBowelQualityData> {
    override val values: Sequence<ConfirmBowelQualityData> = sequenceOf(
        ConfirmBowelQualityData(
            type = TYPE_4,
            date = getDateToday(),
            time = getCurrentTime(),
            showTimeDialog = false,
            showDateDialog = false,
        ),
        ConfirmBowelQualityData(
            type = TYPE_4,
            date = getDateToday(),
            time = getCurrentTime(),
            showTimeDialog = true,
            showDateDialog = false,
        ),
        ConfirmBowelQualityData(
            type = TYPE_4,
            date = getDateToday(),
            time = getCurrentTime(),
            showTimeDialog = false,
            showDateDialog = true,
        )
    )

}


@PreviewLightDark
@Composable
private fun PreviewConfirmBowelQualityScreen(
    @PreviewParameter(ConfirmBowelQualityProvider::class) uiData: ConfirmBowelQualityData
) {
    FibreTrackerTheme {
        ConfirmBowelQualityScreenLayout(
            uiData = uiData,
            onTimeUpdated = { _, _ -> },
            onTypeClicked = {},
            onSubmitClicked = {},
            onDateDialogDismissed = {},
            onDateDialogOpened = {},
            onTimeDialogDismissed = {},
            onTimeDialogOpened = {},
            onDateUpdated = {},
        )
    }
}
