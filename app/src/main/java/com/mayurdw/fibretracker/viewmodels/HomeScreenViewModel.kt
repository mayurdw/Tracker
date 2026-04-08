@file:OptIn(ExperimentalTime::class)

package com.mayurdw.fibretracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayurdw.fibretracker.data.helpers.convertFoodEntryEntityToFoodListItem
import com.mayurdw.fibretracker.data.usecase.IGetEntryUseCase
import com.mayurdw.fibretracker.data.usecase.IGetPoopEntryUseCase
import com.mayurdw.fibretracker.model.domain.EntryData
import com.mayurdw.fibretracker.model.domain.HomeData
import com.mayurdw.fibretracker.model.domain.HomeData.DateData
import com.mayurdw.fibretracker.model.domain.PoopType
import com.mayurdw.fibretracker.model.entity.PoopEntity
import com.mayurdw.fibretracker.viewmodels.UIState.Loading
import com.mayurdw.fibretracker.viewmodels.UIState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit.Companion.DAY
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getEntryUseCase: IGetEntryUseCase,
    private val getPoopEntryUseCase: IGetPoopEntryUseCase,
) : ViewModel() {
    val homeStateFlow: StateFlow<UIState<HomeData>>
        field = MutableStateFlow<UIState<HomeData>>(Loading)

    var currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val todaysDate = currentDate

    fun getLatestData() {
        viewModelScope.launch {
            homeStateFlow.emit(Loading)


            combine(
                getEntryUseCase.getCurrentDateEntryData(currentDate),
                getPoopEntryUseCase.getPoopEntries(currentDate, currentDate)
            ) { current: List<EntryData>, poopEntities: List<PoopEntity> ->
                Pair(
                    current,
                    poopEntities
                )
            }
                .collectLatest {
                    val current = it.first
                    val poopEntries = it.second

                    val foodList = current.map { entryData ->
                        convertFoodEntryEntityToFoodListItem(entryData)
                    }.sortedBy { listItem ->
                        listItem.foodName
                    }

                    val dateFormat = LocalDate.Format {
                        day()
                        char('/')
                        monthNumber()
                        char('/')
                        yearTwoDigits(2000)
                    }
                    val date = currentDate.format(dateFormat)
                    var totalFibre = BigDecimal.ZERO
                    current.forEach { totalFibre += it.fibreConsumedInGms }

                    homeStateFlow.emit(
                        Success(
                            HomeData(
                                hasNext = (todaysDate != currentDate),
                                dateData = DateData(
                                    formattedDate = date,
                                    fibreOfTheDay = totalFibre.toString(),
                                    foodItems = foodList,
                                    poopList = poopEntries.map { poopEntity ->
                                        HomeData.PoopListItem(
                                            id = poopEntity.id,
                                            quality = PoopType.entries[poopEntity.quality]
                                        )
                                    }
                                )
                            )
                        )
                    )
                }
        }
    }

    fun onDateChanged(isPrevious: Boolean) {
        viewModelScope.launch {
            if (isPrevious) {
                currentDate = currentDate.minus(1, DAY)
            } else {
                currentDate = currentDate.plus(1, DAY)
            }

            getLatestData()
        }
    }

}
