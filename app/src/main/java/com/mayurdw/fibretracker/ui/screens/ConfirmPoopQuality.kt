package com.mayurdw.fibretracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mayurdw.fibretracker.R
import com.mayurdw.fibretracker.model.domain.BowelQuality
import com.mayurdw.fibretracker.ui.screens.core.DateDialog
import com.mayurdw.fibretracker.ui.screens.core.LoadingScreen
import com.mayurdw.fibretracker.ui.screens.core.TimeDialog
import com.mayurdw.fibretracker.ui.theme.FibreTrackerTheme
import com.mayurdw.fibretracker.viewmodels.ConfirmPoopQualityUiData
import com.mayurdw.fibretracker.viewmodels.ConfirmPoopQualityViewModel
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleDateDismissed
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleDateOpened
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleNewType
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleSubmission
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleTimeDismissed
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleTimeOpened
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleUpdatedDate
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleUpdatedTime
import com.mayurdw.fibretracker.viewmodels.UIState

@Composable
fun ConfirmPoopQualityScreen(
    type: BowelQuality,
    viewModel: ConfirmPoopQualityViewModel = hiltViewModel(),
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
        is UIState.Loading -> LoadingScreen()
        is UIState.Success -> {
            val poopData: ConfirmPoopQualityUiData =
                (uiState as UIState.Success<*>).data as ConfirmPoopQualityUiData
            ConfirmPoopQualityScreenLayout(
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
fun ConfirmPoopQualityScreenLayout(
    uiData: ConfirmPoopQualityUiData,
    onDateDialogDismissed: () -> Unit,
    onDateDialogOpened: () -> Unit,
    onTimeDialogDismissed: () -> Unit,
    onTimeDialogOpened: () -> Unit,
    onTimeUpdated: (hour: Int, min: Int) -> Unit,
    onDateUpdated: (newDate: Long?) -> Unit,
    onTypeClicked: () -> Unit,
    onSubmitClicked: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                text = "Confirm Details",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Date",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedCard(
                    modifier = Modifier.heightIn(48.dp),
                    onClick = onDateDialogOpened,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = uiData.formattedDate,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Time",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedCard(
                    modifier = Modifier.heightIn(48.dp),
                    onClick = onTimeDialogOpened,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = uiData.formattedTime,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp), onClick = onSubmitClicked
        ) {
            Text(stringResource(R.string.submit))
        }
    }



    if (uiData.showTimeDialog) {
        TimeDialog(
            hour = uiData.hour,
            min = uiData.min,
            onDismiss = onTimeDialogDismissed,
            onConfirmation = onTimeUpdated
        )
    }

    if (uiData.showDateDialog) {
        DateDialog(
            dateInMilliSec = uiData.dateInMilliSec,
            onDismiss = onDateDialogDismissed,
            onConfirmation = onDateUpdated
        )
    }

}


class ConfirmPoopQualityProvider : PreviewParameterProvider<ConfirmPoopQualityUiData> {
    override val values: Sequence<ConfirmPoopQualityUiData> = sequenceOf(
        ConfirmPoopQualityUiData(
            type = BowelQuality.TYPE_4,
            formattedDate = "23/04/26",
            formattedTime = "15.28 pm",
            showTimeDialog = false,
            showDateDialog = false,
            hour = 15,
            min = 28,
            dateInMilliSec = 0L
        ),
        ConfirmPoopQualityUiData(
            type = BowelQuality.TYPE_4,
            formattedDate = "23/04/26",
            formattedTime = "15.28 pm",
            showTimeDialog = true,
            showDateDialog = false,
            hour = 3,
            min = 28,
            dateInMilliSec = 0L
        ),
        ConfirmPoopQualityUiData(
            type = BowelQuality.TYPE_4,
            formattedDate = "23/04/26",
            formattedTime = "15.28 pm",
            showTimeDialog = false,
            showDateDialog = true,
            hour = 15,
            min = 28,
            dateInMilliSec = 0L
        )
    )

}


@PreviewLightDark
@Composable
private fun PreviewConfirmPoopQualityScreen(
    @PreviewParameter(ConfirmPoopQualityProvider::class) uiData: ConfirmPoopQualityUiData
) {
    FibreTrackerTheme {
        ConfirmPoopQualityScreenLayout(
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
