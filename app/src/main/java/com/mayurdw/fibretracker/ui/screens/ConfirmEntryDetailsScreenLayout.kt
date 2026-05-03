package com.mayurdw.fibretracker.ui.screens

import androidx.annotation.StringRes
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mayurdw.fibretracker.R
import com.mayurdw.fibretracker.ui.screens.core.DateDialog
import com.mayurdw.fibretracker.ui.screens.core.TimeDialog
import com.mayurdw.fibretracker.viewmodels.ConfirmBowelQualityData


@Composable
fun ConfirmEntryDetailsScreenLayout(
    entryDetailsLayout: @Composable () -> Unit,
    @StringRes headerTitle: Int,
    formattedDate: String,
    formattedTime: String,
    hour: Int,
    min: Int,
    dateInMilliSec: Long,
    showDateDialog: Boolean,
    showTimeDialog: Boolean,
    onDateDialogDismissed: () -> Unit,
    onDateDialogOpened: () -> Unit,
    onTimeDialogDismissed: () -> Unit,
    onTimeDialogOpened: () -> Unit,
    onTimeUpdated: (hour: Int, min: Int) -> Unit,
    onDateUpdated: (newDate: Long?) -> Unit,
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
                text = stringResource(headerTitle),
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
                        text = formattedDate,
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
                        text = formattedTime,
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



    if (showTimeDialog) {
        TimeDialog(
            hour = hour,
            min = min,
            onDismiss = onTimeDialogDismissed,
            onConfirmation = onTimeUpdated
        )
    }

    if (showDateDialog) {
        DateDialog(
            dateInMilliSec = dateInMilliSec,
            onDismiss = onDateDialogDismissed,
            onConfirmation = onDateUpdated
        )
    }

}