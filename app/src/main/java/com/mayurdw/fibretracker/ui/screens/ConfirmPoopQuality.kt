package com.mayurdw.fibretracker.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.mayurdw.fibretracker.model.domain.PoopType

@Composable
fun ConfirmPoopQualityScreen(type: PoopType) {
    Text("Confirm Poop Quality $type")
}