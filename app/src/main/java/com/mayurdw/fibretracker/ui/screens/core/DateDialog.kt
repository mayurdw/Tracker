package com.mayurdw.fibretracker.ui.screens.core

import android.icu.util.Calendar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.mayurdw.fibretracker.ui.theme.FibreTrackerTheme

@Composable
fun DateDialog(
    dateInMilliSec: Long,
    onDismiss: () -> Unit,
    onConfirmation: (newDateInMilliSec: Long?) -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dateInMilliSec)

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = {
                onConfirmation(datePickerState.selectedDateMillis)
                onDismiss()
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
        DatePicker(state = datePickerState)
    }
}


@PreviewLightDark
@Composable
fun PreviewDateDialog() {
    FibreTrackerTheme {
        DateDialog(
            dateInMilliSec = Calendar.getInstance().timeInMillis,
            onDismiss = {}
        ) { }
    }
}