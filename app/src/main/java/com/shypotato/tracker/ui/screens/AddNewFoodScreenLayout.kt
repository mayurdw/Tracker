package com.shypotato.tracker.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.shypotato.tracker.R
import com.shypotato.tracker.model.domain.CommonFoods
import com.shypotato.tracker.model.entity.FoodEntity
import com.shypotato.tracker.ui.theme.FibreTrackerTheme

@Composable
fun AddNewFoodScreenLayout(
    modifier: Modifier = Modifier,
    data: FoodEntity? = null,
    @StringRes primaryCtaTextId: Int = R.string.add_food,
    buttonIsEnabled: (foodName: String, foodServing: String, fibrePerServing: String) -> Boolean,
    onDelete: () -> Unit = {},
    onAddButton: (foodName: String, foodServing: String, fibrePerServing: String) -> Unit,
) {
    val foodNameState = rememberTextFieldState(data?.name ?: "")
    val foodServingSizeState = rememberTextFieldState(
        data?.let { it.singleServingSizeInGm.toString() } ?: run { "" })
    val fibrePerServingInGms = rememberTextFieldState(
        data?.let { (it.fibrePerGram * it.singleServingSizeInGm.toBigDecimal()).toString() }
            ?: run { "" })

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        TextField(
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            state = foodNameState,
            placeholder = { Text("Food Name") },
            label = { Text("Enter Food Name") },
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            onKeyboardAction = KeyboardActionHandler { focusManager.moveFocus(FocusDirection.Down) }
        )

        TextField(
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            state = foodServingSizeState,
            placeholder = { Text("0") },
            label = { Text("Enter Food Serving Size in Gms") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
            onKeyboardAction = KeyboardActionHandler { focusManager.moveFocus(FocusDirection.Down) }
        )

        TextField(
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            state = fibrePerServingInGms,
            placeholder = { Text("0") },
            label = { Text("Fibre Per Serving in Gms") },
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(
            modifier.weight(1f)
        )

        Button(
            modifier = modifier.fillMaxWidth(),
            enabled =
                buttonIsEnabled(
                    foodNameState.text.toString(),
                    foodServingSizeState.text.toString(),
                    fibrePerServingInGms.text.toString()
                ),
            onClick = {
                onAddButton(
                    foodNameState.text.toString(),
                    foodServingSizeState.text.toString(),
                    fibrePerServingInGms.text.toString()
                )
            }
        ) {
            Text(stringResource(primaryCtaTextId))
        }

        data?.let {
            OutlinedButton(
                modifier = modifier.fillMaxWidth(),
                enabled = true,
                onClick = onDelete,
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

@PreviewDynamicColors
@PreviewLightDark
@Composable
private fun AddNewFoodScreenPreview(
    @PreviewParameter(AddNewFoodScreenParameterProvider::class) data: FoodEntity?
) {
    FibreTrackerTheme {
        AddNewFoodScreenLayout(
            data = data,
            buttonIsEnabled = { _, _, _ ->
                false
            },
            onDelete = {},
            onAddButton = { _, _, _ ->

            },
        )
    }
}

class AddNewFoodScreenParameterProvider : PreviewParameterProvider<FoodEntity?> {
    override val values: Sequence<FoodEntity?> = sequenceOf(
        null,
        CommonFoods[1]
    )
}
