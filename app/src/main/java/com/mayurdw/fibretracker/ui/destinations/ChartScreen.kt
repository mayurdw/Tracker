package com.mayurdw.fibretracker.ui.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.ZeroLineProperties

@Preview
@Composable
fun ChartScreen() {
    LineChart(
        data = remember {
            listOf(
                Line(
                    label = "Temperature",
                    values = listOf(28.0, 41.0, -15.0, 27.0, 54.0),
                    color = Brush.radialGradient(
                        listOf(Color.Blue, Color.Cyan)
                    )
                )
            )
        },
        zeroLineProperties = ZeroLineProperties(
            enabled = true,
            color = SolidColor(Color.Red),
        ),
        minValue = -20.0,
        maxValue = 100.0
    )
}