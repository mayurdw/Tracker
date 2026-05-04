package com.mayurdw.fibretracker.ui.screens

import androidx.annotation.StringRes
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
import com.mayurdw.fibretracker.ui.screens.core.DateDialog
import com.mayurdw.fibretracker.ui.screens.core.TimeDialog
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime


@Composable
fun ConfirmEntryDetailsScreenLayout(
    entryDetailsLayout: @Composable () -> Unit,
    footerDetailsLayout: @Composable () -> Unit,
    headerTitle: String,
    time: LocalTime,
    date: LocalDate,
    showDateDialog: Boolean,
    showTimeDialog: Boolean,
    canDelete: Boolean,
    buttonEnabled: Boolean,
    onDateDialogDismissed: () -> Unit,
    onDateDialogOpened: () -> Unit,
    onTimeDialogDismissed: () -> Unit,
    onTimeDialogOpened: () -> Unit,
    onTimeUpdated: (hour: Int, min: Int) -> Unit,
    onDateUpdated: (newDate: Long?) -> Unit,
    onDeleteClicked: () -> Unit,
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
                text = headerTitle,
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
                    onClick = onDateDialogOpened,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = date.getFormattedDate(),
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
                    onClick = onTimeDialogOpened,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = time.getFormattedTime(),
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
                onClick = onSubmitClicked,
                enabled = buttonEnabled
            ) {
                Text(stringResource(R.string.submit))
            }

            if (canDelete) {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true,
                    onClick = onDeleteClicked,
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



    if (showTimeDialog) {
        TimeDialog(
            hour = time.hour,
            min = time.minute,
            onDismiss = onTimeDialogDismissed,
            onConfirmation = onTimeUpdated
        )
    }

    if (showDateDialog) {
        DateDialog(
            dateInMilliSec = date.toDateInMilliSecs(),
            onDismiss = onDateDialogDismissed,
            onConfirmation = onDateUpdated
        )
    }
}

@Composable
fun ConfirmEntryDetailsScreenLayout(
    entryDetailsLayout: @Composable () -> Unit,
    footerDetailsLayout: @Composable () -> Unit,
    @StringRes headerTitle: Int,
    time: LocalTime,
    date: LocalDate,
    showDateDialog: Boolean,
    showTimeDialog: Boolean,
    canDelete: Boolean,
    buttonEnabled: Boolean,
    onDateDialogDismissed: () -> Unit,
    onDateDialogOpened: () -> Unit,
    onTimeDialogDismissed: () -> Unit,
    onTimeDialogOpened: () -> Unit,
    onTimeUpdated: (hour: Int, min: Int) -> Unit,
    onDateUpdated: (newDate: Long?) -> Unit,
    onDeleteClicked: () -> Unit,
    onSubmitClicked: () -> Unit,
) {
    ConfirmEntryDetailsScreenLayout(
        entryDetailsLayout,
        footerDetailsLayout,
        stringResource(headerTitle),
        time,
        date,
        showDateDialog,
        showTimeDialog,
        canDelete,
        buttonEnabled,
        onDateDialogDismissed,
        onDateDialogOpened,
        onTimeDialogDismissed,
        onTimeDialogOpened,
        onTimeUpdated,
        onDateUpdated,
        onDeleteClicked,
        onSubmitClicked
    )
}