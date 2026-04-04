package com.mayurdw.fibretracker.ui.screens

import android.icu.util.Calendar
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.mayurdw.fibretracker.R
import com.mayurdw.fibretracker.model.domain.PoopType
import com.mayurdw.fibretracker.ui.screens.core.DateDialog
import com.mayurdw.fibretracker.ui.screens.core.TimeDialog
import com.mayurdw.fibretracker.ui.theme.FibreTrackerTheme

@Composable
fun ConfirmPoopQualityScreen(type: PoopType) {

}

@Composable
fun ConfirmPoopQualityScreenLayout(
    type: PoopType,
    formattedTime: String,
    formattedDate: String,
    showTimeDialog: Boolean,
    showDateDialog: Boolean,
    onTimeUpdated: (hour: Int, min: Int) -> Unit,
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
                        text = type.title,
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
                    onClick = {},
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
                    onClick = {},
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
            hour = 12,
            min = 23,
            onDismiss = { },
            onConfirmation = onTimeUpdated
        )
    }

    if (showDateDialog) {
        DateDialog(
            dateInMilliSec = Calendar.getInstance().timeInMillis,
            onDismiss = {}
        ) { }
    }

}


@PreviewLightDark
@Composable
private fun PreviewConfirmPoopQualityScreen() {
    FibreTrackerTheme {
        ConfirmPoopQualityScreenLayout(
            type = PoopType.TYPE_4,
            formattedDate = "23/04/26",
            formattedTime = "15.28 pm",
            showTimeDialog = false,
            showDateDialog = false,
            onTimeUpdated = { _, _ -> },
            onTypeClicked = {},
            onSubmitClicked = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewDateDialog() {
    FibreTrackerTheme {
        ConfirmPoopQualityScreenLayout(
            type = PoopType.TYPE_4,
            formattedDate = "23/04/26",
            formattedTime = "15.28 pm",
            showTimeDialog = false,
            showDateDialog = true,
            onTimeUpdated = { _, _ -> },
            onTypeClicked = {},
            onSubmitClicked = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewTimeDialog() {
    FibreTrackerTheme {
        ConfirmPoopQualityScreenLayout(
            type = PoopType.TYPE_4,
            formattedDate = "23/04/26",
            formattedTime = "15.28 pm",
            showTimeDialog = true,
            showDateDialog = false,
            onTimeUpdated = { _, _ -> },
            onTypeClicked = {},
            onSubmitClicked = {}
        )
    }
}