package com.mayurdw.fibretracker.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mayurdw.fibretracker.R
import com.mayurdw.fibretracker.data.helpers.getFormattedDate
import com.mayurdw.fibretracker.data.helpers.getFormattedTime
import com.mayurdw.fibretracker.data.helpers.toDateInMilliSecs
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent.Delete
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent.DismissDate
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent.DismissTime
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent.OpenDate
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent.OpenTime
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent.Submit
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent.UpdateDate
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent.UpdateTime
import com.mayurdw.fibretracker.ui.screens.core.DateDialog
import com.mayurdw.fibretracker.ui.screens.core.TimeDialog

@Composable
fun ConfirmEntryDetailsScreenLayout(
    detailsData: ConfirmEntryDetailsData,
    entryDetailsLayout: @Composable () -> Unit,
    footerDetailsLayout: @Composable () -> Unit,
    onUserEvent: (detailsIntent: ConfirmEntryDetailsIntent) -> Unit
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
                text = detailsData.title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            entryDetailsLayout()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.date),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedCard(
                    modifier = Modifier.heightIn(48.dp),
                    onClick = { onUserEvent(OpenDate) }
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = detailsData.date.getFormattedDate(),
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
                    text = stringResource(R.string.time),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedCard(
                    modifier = Modifier.heightIn(48.dp),
                    onClick = { onUserEvent(OpenTime) }
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = detailsData.time.getFormattedTime(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            footerDetailsLayout()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onUserEvent(Submit) },
                enabled = detailsData.submitEnabled
            ) {
                Text(stringResource(R.string.submit))
            }

            if (detailsData.canDelete) {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true,
                    onClick = { onUserEvent(Delete) },
                    border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.error)
                ) {
                    Text(
                        text = "Delete",
                        color = MaterialTheme.colorScheme.error
                    )

                }
            }
        }
    }



    if (detailsData.showTimeDialog) {
        TimeDialog(
            hour = detailsData.time.hour,
            min = detailsData.time.minute,
            onDismiss = { onUserEvent(DismissTime) },
            onConfirmation = { hour, min ->
                onUserEvent(
                    UpdateTime(
                        hour,
                        min
                    )
                )
            }
        )
    }

    if (detailsData.showDateDialog) {
        DateDialog(
            dateInMilliSec = detailsData.date.toDateInMilliSecs(),
            onDismiss = { onUserEvent(DismissDate) },
            onConfirmation = {
                onUserEvent(UpdateDate(it))
            }
        )
    }
}