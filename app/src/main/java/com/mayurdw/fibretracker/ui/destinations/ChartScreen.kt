package com.mayurdw.fibretracker.ui.destinations

import android.R
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mayurdw.fibretracker.model.domain.BowelType
import com.mayurdw.fibretracker.model.domain.UIState
import com.mayurdw.fibretracker.ui.screens.core.LoadingScreen
import com.mayurdw.fibretracker.viewmodels.ChartData
import com.mayurdw.fibretracker.viewmodels.ChartScreenViewModel
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.StrokeStyle

@Composable
fun ChartScreen(
    viewModel: ChartScreenViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.loadData()
    }

    when (state) {
        is UIState.Loading -> {
            LoadingScreen()
        }

        is UIState.Error -> {}
        is UIState.Success<*> -> {
            val dataset = (state as UIState.Success<ChartData>).data

            ChartScreenLayout()
        }
    }
}

@PreviewLightDark
@Composable
fun ChartScreenLayout(
    dataset: ChartData = ChartData(
        dates = listOf(
            "00",
            "01",
            "02"
        ),
        fibres = listOf(
            23.0,
            23.0,
            23.0,
            20.0,
            15.0,
            20.0,
            15.0,
            20.0,
            15.0,
            10.0
        )
    )
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(16.dp)
    ) {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f)
                .padding(horizontal = 22.dp),
            data = remember {
                listOf(
                    Line(
                        label = "Fibre",
                        values = dataset.fibres,
                        color = SolidColor(Color.Magenta),
                        drawStyle = DrawStyle.Stroke(
                            width = 2.dp,
                            strokeStyle = StrokeStyle.Dashed()
                        ),
                        dotProperties = DotProperties(
                            enabled = true,
                            color = SolidColor(Color.White),
                            strokeWidth = 4.dp,
                            radius = 7.dp,
                            strokeColor = SolidColor(Color.Magenta),
                        )
                    ),
                    Line(
                        label = "Bowel Quality",
                        values = BowelType.entries.map { it.ordinal.toDouble() },
                        color = SolidColor(Color.Green),
                        drawStyle = DrawStyle.Stroke(
                            width = 2.dp,
                            strokeStyle = StrokeStyle.Dashed()
                        ),
                        dotProperties = DotProperties(
                            enabled = true,
                            color = SolidColor(Color.White),
                            strokeWidth = 4.dp,
                            radius = 7.dp,
                            strokeColor = SolidColor(Color.Green),
                        )
                    )
                )
            },
            curvedEdges = false,
            gridProperties = GridProperties(enabled = false),
            labelProperties = LabelProperties(enabled = true, labels = dataset.dates)
        )
    }
}
