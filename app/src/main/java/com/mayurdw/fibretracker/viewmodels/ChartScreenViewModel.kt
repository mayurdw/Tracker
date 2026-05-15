package com.mayurdw.fibretracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayurdw.fibretracker.data.usecase.GetEntriesUseCase
import com.mayurdw.fibretracker.model.domain.UIState
import com.mayurdw.fibretracker.model.domain.UIState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChartData(
    val dates: List<String>,
    val fibres: List<Double>,
)

@HiltViewModel
class ChartScreenViewModel @Inject constructor(
    private val getEntriesUseCase: GetEntriesUseCase
) : ViewModel() {
    val uiState: StateFlow<UIState<ChartData>>
        field = MutableStateFlow<UIState<ChartData>>(Loading)

    fun loadData() {
        viewModelScope.launch {
            uiState.emit(UIState.Success(ChartData(emptyList(), emptyList())))
//            val endDate = getCurrentDate()
//            val startDate = endDate.minus(7, DAY)
//
//            getEntriesUseCase(
//                startDate,
//                endDate
//            ).collectLatest { entries ->
//                var index = 0
//
//                while (index < 7) {
//                    index++
//                    val currentDate = startDate.plus(index, DAY)
//                    var fibreOfDay = ZERO
//
//                    val count = entries.count { it.date == currentDate }
//
//                    if (count > 0) {
//                        entries.filter { it.info is Food && it.date == currentDate }
//                            .onEach { fibreOfDay += (it.info as Food).fibreConsumedInGms }
//                    }
//                }
//            }
        }
    }
}