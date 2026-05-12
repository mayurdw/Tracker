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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.mayurdw.fibretracker.R
import com.mayurdw.fibretracker.data.helpers.getCurrentDate
import com.mayurdw.fibretracker.data.helpers.getCurrentTime
import com.mayurdw.fibretracker.model.domain.BowelType.TYPE_4
import com.mayurdw.fibretracker.model.domain.ConfirmEntryDetailsData
import com.mayurdw.fibretracker.model.domain.ConfirmEntryDetailsIntent
import com.mayurdw.fibretracker.ui.theme.FibreTrackerTheme
import com.mayurdw.fibretracker.model.domain.ConfirmBowelQualityData


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
            date = getCurrentDate(),
            time = getCurrentTime(),
            showTimeDialog = false,
            showDateDialog = false,
        ),
        ConfirmBowelQualityData(
            type = TYPE_4,
            date = getCurrentDate(),
            time = getCurrentTime(),
            showTimeDialog = true,
            showDateDialog = false,
        ),
        ConfirmBowelQualityData(
            type = TYPE_4,
            date = getCurrentDate(),
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