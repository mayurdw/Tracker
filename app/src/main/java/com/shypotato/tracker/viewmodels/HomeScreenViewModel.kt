@file:OptIn(ExperimentalTime::class)

package com.shypotato.tracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shypotato.tracker.data.helpers.getCurrentDate
import com.shypotato.tracker.data.helpers.getFormattedDate
import com.shypotato.tracker.data.usecase.IGetEntriesUseCase
import com.shypotato.tracker.model.domain.HomeData
import com.shypotato.tracker.model.domain.HomeData.DateData
import com.shypotato.tracker.model.domain.UIState
import com.shypotato.tracker.model.domain.EntryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit.Companion.DAY
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import java.math.BigDecimal.ZERO
import javax.inject.Inject
import kotlin.plus
import kotlin.time.ExperimentalTime

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getEntries: IGetEntriesUseCase
) : ViewModel() {
    val homeStateFlow: StateFlow<UIState<HomeData>>
        field = MutableStateFlow<UIState<HomeData>>(UIState.Loading)

    var currentDate: LocalDate = getCurrentDate()
    val todaysDate = currentDate

    fun getLatestData() {
        viewModelScope.launch {
            homeStateFlow.emit(UIState.Loading)

            getEntries(currentDate, currentDate).collectLatest { entries ->
                var fibreForDay = ZERO
                entries.filter { it.info is EntryType.Food }
                    .onEach { fibreForDay + (it.info as EntryType.Food).fibreConsumedInGms }

                homeStateFlow.emit(
                    UIState.Success(
                        HomeData(
                            hasNext = todaysDate != currentDate,
                            dateData = DateData(
                                fibreOfTheDay = fibreForDay.toString(),
                                listItem = entries,
                                formattedDate = currentDate.getFormattedDate()
                            )
                        )
                    )
                )
            }
        }
    }

    fun onDateChanged(isPrevious: Boolean) {
        viewModelScope.launch {
            currentDate = if (isPrevious) {
                currentDate.minus(1, DAY)
            } else {
                currentDate.plus(1, DAY)
            }

            getLatestData()
        }
    }
}
