package com.mayurdw.fibretracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayurdw.fibretracker.data.helpers.getCurrentDate
import com.mayurdw.fibretracker.data.helpers.getFormattedDate
import com.mayurdw.fibretracker.data.usecase.GetEntriesUseCase
import com.mayurdw.fibretracker.model.domain.EntryType
import com.mayurdw.fibretracker.model.domain.UIState
import com.mayurdw.fibretracker.model.domain.UIState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DateTimeUnit.Companion.DAY
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import java.math.BigDecimal
import javax.inject.Inject

data class ChartData(
    val dates: List<String>,
    val fibres: List<Double>,
    val bowels: List<Double>
)

@HiltViewModel
class ChartScreenViewModel @Inject constructor(
    private val getEntriesUseCase: GetEntriesUseCase
) : ViewModel() {
    val uiState: StateFlow<UIState<ChartData>>
        field = MutableStateFlow<UIState<ChartData>>(Loading)

    fun loadData() {
        viewModelScope.launch {
            val endDate = getCurrentDate()
            val startDate = endDate.minus(1, DateTimeUnit.WEEK)

            val entries = getEntriesUseCase(startDate, endDate).first()

            val fibres: List<Double> = buildList {
                var x = 1

                while (x < 7) {
                    val currentDate = startDate.plus(x, DAY)
                    var fibreOfDay = BigDecimal.ZERO

                    entries.filter { currentDate == it.date && it.info is EntryType.Food }
                        .onEach { fibreOfDay += (it.info as EntryType.Food).fibreConsumedInGms }

                    add(fibreOfDay.toDouble())
                    x++
                }
            }

            val bowels: List<Double> = buildList {
                var x = 1

                while (x < 7) {
                    val currentDate = startDate.plus(x, DAY)
                    var bowel = 0
                    val count =
                        entries.count { currentDate == it.date && it.info is EntryType.Bowel }

                    entries.filter { currentDate == it.date && it.info is EntryType.Bowel }
                        .onEach { bowel += (it.info as EntryType.Bowel).quality.ordinal }

                    if (count > 0) {
                        add((bowel / count).toDouble())
                    } else {
                        add(0.toDouble())
                    }
                    x++
                }
            }

            val dates = buildList {
                add(startDate.getFormattedDate())
                add(startDate.plus(4, DAY).getFormattedDate())
                add(endDate.getFormattedDate())
            }

            uiState.emit(
                UIState.Success(
                    ChartData(
                        dates = dates,
                        fibres = fibres,
                        bowels = bowels
                    )
                )
            )
        }
    }
}