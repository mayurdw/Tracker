package com.shypotato.tracker.ui.screens

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
import com.shypotato.tracker.R
import com.shypotato.tracker.data.helpers.getCurrentDate
import com.shypotato.tracker.data.helpers.getCurrentTime
import com.shypotato.tracker.model.domain.BowelType
import com.shypotato.tracker.model.domain.ConfirmData
import com.shypotato.tracker.model.domain.ConfirmDataType.Bowel
import com.shypotato.tracker.model.domain.ConfirmEntryDetailsData
import com.shypotato.tracker.model.domain.ConfirmEntryDetailsIntent
import com.shypotato.tracker.ui.theme.FibreTrackerTheme


@Composable
fun ConfirmBowelQualityScreenLayout(
    uiData: ConfirmData,
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
            canDelete = uiData.canDelete,
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
                        text = (uiData.type as Bowel).type.title,
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


class ConfirmBowelQualityProvider : PreviewParameterProvider<ConfirmData> {
    override val values: Sequence<ConfirmData> = sequenceOf(
        ConfirmData(
            type = Bowel(BowelType.TYPE_4),
            date = getCurrentDate(),
            time = getCurrentTime(),
            showTimeDialog = false,
            showDateDialog = false,
            canDelete = false,
            submitEnabled = true
        ),
        ConfirmData(
            type = Bowel(BowelType.TYPE_4),
            date = getCurrentDate(),
            time = getCurrentTime(),
            showTimeDialog = true,
            showDateDialog = false,
            canDelete = false,
            submitEnabled = true
        ),
        ConfirmData(
            type = Bowel(BowelType.TYPE_4),
            date = getCurrentDate(),
            time = getCurrentTime(),
            showTimeDialog = false,
            showDateDialog = true,
            canDelete = false,
            submitEnabled = true
        )
    )

}


@PreviewLightDark
@Composable
private fun PreviewConfirmBowelQualityScreen(
    @PreviewParameter(ConfirmBowelQualityProvider::class) uiData: ConfirmData
) {
    FibreTrackerTheme {
        ConfirmBowelQualityScreenLayout(
            uiData = uiData,
            onTypeClicked = {}
        ) {

        }
    }
}