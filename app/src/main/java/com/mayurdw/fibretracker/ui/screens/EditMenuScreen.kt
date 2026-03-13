package com.mayurdw.fibretracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.mayurdw.fibretracker.R
import com.mayurdw.fibretracker.ui.theme.FibreTrackerTheme

@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun PreviewEditMenuScreen() {
    FibreTrackerTheme {
        EditMenuScreen(onAddClicked = {}, onEditClicked = {})
    }
}

@Composable
fun EditMenuScreen(
    modifier: Modifier = Modifier,
    onAddClicked: () -> Unit,
    onEditClicked: () -> Unit
) {
    Column(
        modifier = modifier.padding(top = 24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = onAddClicked,
            modifier = modifier.fillMaxWidth(),
        )
        {
            Row(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = modifier.size(24.dp),
                    imageVector = Icons.Filled.Add,
                    contentDescription = ""
                )

                Spacer(modifier = modifier.width(8.dp))

                Text(
                    modifier = modifier.weight(1.0f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    text = stringResource(R.string.add_new_food)
                )
            }
        }

        Spacer(modifier.height(16.dp))

        Button(onClick = onEditClicked, modifier = modifier.fillMaxWidth()) {
            Row(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = modifier.size(24.dp),
                    imageVector = Icons.Filled.Edit,
                    contentDescription = ""
                )

                Spacer(modifier = modifier.width(8.dp))

                Text(
                    modifier = modifier.weight(1.0f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    text = stringResource(R.string.edit_existing_food)
                )
            }
        }
    }
}
