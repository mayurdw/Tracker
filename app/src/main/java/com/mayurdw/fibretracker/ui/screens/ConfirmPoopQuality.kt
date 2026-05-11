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
import androidx.compose.ui.res.stringResource
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
import com.mayurdw.fibretracker.viewmodels.ConfirmBowelQualityIntents.HandleNewType
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
                onTypeClicked = onTypeClicked,
                onUserEvent = { viewModel.onUserEvent(it) }
            )
        }

        else -> {}
    }

}

@Composable
fun ConfirmBowelQualityScreenLayout(
    uiData: ConfirmBowelQualityData,
    onTypeClicked: () -> Unit,
    onUserEvent: (detailsIntent: ConfirmEntryDetailsIntent) -> Unit,
) {

    ConfirmEntryDetailsScreenLayout(
        detailsData = ConfirmEntryDetailsData(
            title = stringResource(R.string.confirm_details),
            time = uiData.time,
            date = uiData.date,
            showDateDialog = uiData.showDateDialog,
            showTimeDialog = uiData.showTimeDialog,
            canDelete = false,
            submitEnabled = true,
        ),
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
        onUserEvent = onUserEvent
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
            onTypeClicked = {}
        ) {

        }
    }
}
