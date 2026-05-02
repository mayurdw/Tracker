@file:OptIn(ExperimentalTime::class)

package com.mayurdw.fibretracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayurdw.fibretracker.data.helpers.getDateToday
import com.mayurdw.fibretracker.data.helpers.getFormattedDate
import com.mayurdw.fibretracker.data.usecase.IGetEntriesUseCase
import com.mayurdw.fibretracker.model.domain.EntryType.Food
import com.mayurdw.fibretracker.model.domain.HomeData
import com.mayurdw.fibretracker.model.domain.HomeData.DateData
import com.mayurdw.fibretracker.viewmodels.UIState.Loading
import com.mayurdw.fibretracker.viewmodels.UIState.Success
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
import kotlin.time.ExperimentalTime

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getEntries: IGetEntriesUseCase
) : ViewModel() {
    val homeStateFlow: StateFlow<UIState<HomeData>>
        field = MutableStateFlow<UIState<HomeData>>(Loading)

    var currentDate: LocalDate = getDateToday()
    val todaysDate = currentDate

    fun getLatestData() {
        viewModelScope.launch {
            homeStateFlow.emit(Loading)

            getEntries(currentDate, currentDate).collectLatest { entries ->
                var fibreForDay = ZERO
                entries.filter { it.info is Food }
                    .onEach { fibreForDay += (it.info as Food).fibreConsumedInGms }

                homeStateFlow.emit(
                    Success(
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
