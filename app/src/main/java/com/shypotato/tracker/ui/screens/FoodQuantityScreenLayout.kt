package com.shypotato.tracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.shypotato.tracker.R
import com.shypotato.tracker.data.helpers.getCurrentDate
import com.shypotato.tracker.data.helpers.getCurrentTime
import com.shypotato.tracker.model.domain.ConfirmData
import com.shypotato.tracker.model.domain.ConfirmDataType
import com.shypotato.tracker.model.domain.ConfirmDataType.Food
import com.shypotato.tracker.model.domain.ConfirmEntryDetailsData
import com.shypotato.tracker.model.domain.ConfirmEntryDetailsIntent
import com.shypotato.tracker.model.entity.FoodEntity
import com.shypotato.tracker.ui.theme.FibreTrackerTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FoodQuantityScreenLayout(
    modifier: Modifier = Modifier,
    uiData: ConfirmData,
    quantityUpdated: (foodQuantity: String?) -> Unit,
    onUserEvent: (detailsIntent: ConfirmEntryDetailsIntent) -> Unit,
) {
    val foodQuantity =
        rememberTextFieldState(
            initialText =
                (uiData.type as ConfirmDataType.Food).entity.singleServingSizeInGm.toString()
        )
    var fibreQuantity =
        if (foodQuantity.text.isNotBlank()) {
            (uiData.type as ConfirmDataType.Food).entity.fibrePerGram * foodQuantity.text.toString()
                .toBigDecimal()
        } else {
            0
        }

    LaunchedEffect(foodQuantity) {
        snapshotFlow { foodQuantity.text.toString() }.collectLatest { newValue: String ->
            quantityUpdated(newValue)
            if (newValue.isNotBlank()) {
                fibreQuantity =
                    (uiData.type as ConfirmDataType.Food).entity.fibrePerGram * newValue.toBigDecimal()
            }
        }
    }

    ConfirmEntryDetailsScreenLayout(
        entryDetailsLayout = {
            Column(
                modifier = modifier
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                TextField(
                    modifier = modifier
                        .fillMaxWidth(),
                    state = foodQuantity,
                    label = { Text(stringResource(R.string.quantity_in_grams)) },
                    placeholder = { Text("${uiData.type.entity.singleServingSizeInGm}") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine,
                )
            }
        },
        footerDetailsLayout = {
            Column(
                modifier = modifier
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    modifier = modifier,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = modifier,
                        text = stringResource(R.string.fibre_per_gram),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = modifier.weight(0.5f))

                    Text(
                        modifier = modifier,
                        text = stringResource(
                            R.string.var_gm,
                            uiData.type.entity.fibrePerGram.toString()
                        ),
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
                        text = stringResource(R.string.fibre_consumed),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = modifier.weight(0.5f))

                    Text(
                        modifier = modifier,
                        text = "$fibreQuantity" + stringResource(R.string.gm),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        detailsData = ConfirmEntryDetailsData(
            title = uiData.type.entity.name,
            time = uiData.time,
            date = uiData.date,
            showDateDialog = uiData.showDateDialog,
            showTimeDialog = uiData.showTimeDialog,
            canDelete = uiData.canDelete,
            submitEnabled = uiData.submitEnabled,
        ),
        onUserEvent = onUserEvent,
    )
}

@PreviewLightDark
@Composable
private fun FoodQuantityScreenPreview(
    @PreviewParameter(FoodQuantityScreenProvider::class) deleteEnabled: Boolean
) {
    FibreTrackerTheme {
        FoodQuantityScreenLayout(
            uiData = ConfirmData(
                type = Food(
                    entity = FoodEntity(
                        name = "Test",
                        singleServingSizeInGm = 40,
                        fibrePerMicroGram = 1_000_00
                    ),
                    foodQuantity = "4"
                ),
                time = getCurrentTime(),
                date = getCurrentDate(),
                showDateDialog = false,
                showTimeDialog = false,
                submitEnabled = false,
                canDelete = false,
            ),
            quantityUpdated = {}
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
