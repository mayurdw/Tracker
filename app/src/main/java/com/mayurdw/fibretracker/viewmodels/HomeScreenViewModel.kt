@file:OptIn(ExperimentalTime::class)

package com.mayurdw.fibretracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayurdw.fibretracker.data.helpers.convertFoodEntryEntityToFoodListItem
import com.mayurdw.fibretracker.data.helpers.getDateToday
import com.mayurdw.fibretracker.data.helpers.getFormattedDate
import com.mayurdw.fibretracker.data.helpers.getFormattedTime
import com.mayurdw.fibretracker.data.usecase.IGetBowelMovementEntryUseCase
import com.mayurdw.fibretracker.data.usecase.IGetFoodEntriesUseCase
import com.mayurdw.fibretracker.model.domain.FoodEntryData
import com.mayurdw.fibretracker.model.domain.HomeData
import com.mayurdw.fibretracker.model.domain.HomeData.DateData
import com.mayurdw.fibretracker.model.domain.ListItem
import com.mayurdw.fibretracker.model.domain.ListItem.FoodListItem
import com.mayurdw.fibretracker.model.domain.PoopType.entries
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
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import java.math.BigDecimal.ZERO
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getEntries: IGetFoodEntriesUseCase,
    private val getBowelMovements: IGetBowelMovementEntryUseCase,
) : ViewModel() {
    val homeStateFlow: StateFlow<UIState<HomeData>>
        field = MutableStateFlow<UIState<HomeData>>(Loading)

    var currentDate: LocalDate = getDateToday()
    val todaysDate = currentDate

    fun getLatestData() {
        viewModelScope.launch {
            homeStateFlow.emit(Loading)

            combine(
                getEntries(currentDate),
                getBowelMovements(currentDate)
            ) { current: List<FoodEntryData>, poopEntities: List<PoopEntity> ->
                Pair(
                    current,
                    poopEntities
                )
            }
                .collectLatest {
                    val current = it.first
                    val poopEntries = it.second
                    var index = -1

                    val foodList: List<FoodListItem> =
                        current.map { entryData ->
                            index += 1
                            convertFoodEntryEntityToFoodListItem(entryData, index)
                        }.sortedBy { listItem ->
                            listItem.foodName
                        }

                    val date = currentDate.getFormattedDate()
                    var totalFibre = ZERO
                    current.forEach { food -> totalFibre += food.fibreConsumedInGms }


                    val poopList = poopEntries.map { poopEntity ->
                        index += 1
                        ListItem.PoopListItem(
                            itemId = index,
                            id = poopEntity.id,
                            quality = entries[poopEntity.quality],
                            time = poopEntity.time.getFormattedTime()
                        )
                    }
                    val listItems: List<ListItem> = buildList {
                        addAll(foodList)
                        addAll(poopList)
                    }

                    homeStateFlow.emit(
                        Success(
                            HomeData(
                                hasNext = (todaysDate != currentDate),
                                dateData = DateData(
                                    formattedDate = date,
                                    fibreOfTheDay = totalFibre.toString(),
                                    listItem = listItems
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
