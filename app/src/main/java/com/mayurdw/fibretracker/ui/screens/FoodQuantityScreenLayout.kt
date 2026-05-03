package com.mayurdw.fibretracker.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.mayurdw.fibretracker.R
import com.mayurdw.fibretracker.ui.theme.FibreTrackerTheme
import kotlinx.coroutines.flow.collectLatest
import java.math.BigDecimal

@Composable
fun FoodQuantityScreenLayout(
    modifier: Modifier = Modifier,
    singleServingSizeInGm: Int,
    foodName: String,
    fibrePerGram: BigDecimal,
    canDelete: Boolean,
    buttonEnabled: (foodQuantity: String?) -> Boolean,
    onDeleteClicked: () -> Unit,
    onSaveClick: (foodQuantity: String) -> Unit
) {
    val foodQuantity =
        rememberTextFieldState(initialText = singleServingSizeInGm.toString())
    var fibreQuantity =
        if (foodQuantity.text.isNotBlank()) {
            fibrePerGram * foodQuantity.text.toString().toBigDecimal()
        } else {
            0
        }

    LaunchedEffect(foodQuantity) {
        snapshotFlow { foodQuantity.text.toString() }.collectLatest { newValue: String ->
            if (newValue.isNotBlank()) {
                fibreQuantity =
                    fibrePerGram * newValue.toBigDecimal()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = foodName,
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        TextField(
            modifier = modifier.fillMaxWidth(),
            state = foodQuantity,
            label = { Text("Quantity in Grams") },
            placeholder = { Text("$singleServingSizeInGm") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
        )

        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = modifier,
                text = "Fibre Per Gram",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = modifier.weight(0.5f))

            Text(
                modifier = modifier,
                text = "$fibrePerGram gm",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }


        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = modifier,
                text = "Fibre consumed",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = modifier.weight(0.5f))

            Text(
                modifier = modifier,
                text = "$fibreQuantity gm",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = modifier.weight(0.5f))

        Button(
            modifier = modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            onClick = {
                onSaveClick(foodQuantity.text.toString())
            },
            enabled = buttonEnabled(foodQuantity.text.toString()),
            content = { Text(stringResource(R.string.submit)) })

        if (canDelete) {
            OutlinedButton(
                modifier = modifier.fillMaxWidth(),
                enabled = true,
                onClick = onDeleteClicked,
                border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.error)
            ) {
                Text(
                    modifier = modifier,
                    text = "Delete",
                    color = MaterialTheme.colorScheme.error
                )

            }
        }
    }
}

@PreviewLightDark
@Composable
private fun FoodQuantityScreenPreview(
    @PreviewParameter(FoodQuantityScreenProvider::class) deleteEnabled: Boolean
) {
    FibreTrackerTheme {
        FoodQuantityScreenLayout(
            foodName = "Test",
            singleServingSizeInGm = 40,
            fibrePerGram = BigDecimal.ONE,
            buttonEnabled = { true },
            onDeleteClicked = {},
            canDelete = deleteEnabled
        ) { _ ->

        }
    }
}

class FoodQuantityScreenProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(
        true,
        false
    )
}
