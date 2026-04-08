package com.mayurdw.fibretracker.ui.screens.core

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.mayurdw.fibretracker.ui.theme.FibreTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDialog(
    hour: Int,
    min: Int,
    onDismiss: () -> Unit,
    onConfirmation: (
        hour: Int,
        min: Int
    ) -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = min,
        is24Hour = false
    )

    TimePickerDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                modifier = Modifier.fillMaxWidth().padding(all = 8.dp),
                text = "Select Time",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodySmall
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmation(timePickerState.hour, timePickerState.minute)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        TimePicker(state = timePickerState)
    }
}


@PreviewLightDark
@Composable
fun PreviewTimeDialog() {
    FibreTrackerTheme {
        TimeDialog(
            hour = 13,
            min = 34,
            onDismiss = {}
        ) { _, _ -> }
    }
}