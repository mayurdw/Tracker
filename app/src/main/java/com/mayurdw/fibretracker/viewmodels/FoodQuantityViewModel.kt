package com.mayurdw.fibretracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayurdw.fibretracker.data.helpers.getCurrentTime
import com.mayurdw.fibretracker.data.helpers.getDateToday
import com.mayurdw.fibretracker.data.usecase.IAddEntryUseCase
import com.mayurdw.fibretracker.data.usecase.IGetFoodUseCase
import com.mayurdw.fibretracker.model.entity.FoodEntity
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent.Delete
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent.DismissDate
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent.DismissTime
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent.None
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent.OpenDate
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent.OpenTime
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent.Submit
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent.UpdateDate
import com.mayurdw.fibretracker.ui.screens.ConfirmEntryDetailsIntent.UpdateTime
import com.mayurdw.fibretracker.viewmodels.UIState.Error
import com.mayurdw.fibretracker.viewmodels.UIState.Loading
import com.mayurdw.fibretracker.viewmodels.UIState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate.Companion.fromEpochDays
import kotlinx.datetime.LocalTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class FoodQuantityViewModel @Inject constructor(
    private val getFood: IGetFoodUseCase,
    private val addEntry: IAddEntryUseCase
) : ViewModel() {
    val uiState: StateFlow<UIState<FoodQuantityData>>
        field = MutableStateFlow<UIState<FoodQuantityData>>(Loading)
    private lateinit var _uiData: FoodQuantityData

    val entryState: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    fun loadFoodDetails(id: Int) {
        viewModelScope.launch {
            uiState.emit(Loading)
            getFood(id).collectLatest {
                if (it.isSuccess) {
                    _uiData = FoodQuantityData(
                        it.getOrNull()!!,
                        time = getCurrentTime(),
                        date = getDateToday(),
                        showDateDialog = false,
                        showTimeDialog = false,
                        submitEnabled = false,
                        canDelete = false
                    )
                    uiState.emit(Success(_uiData))
                } else {
                    uiState.emit(Error)
                }
            }
        }
    }

    fun handleQuantityChange(newValue: String?) {
        viewModelScope.launch {
            _uiData = _uiData.copy(
                submitEnabled = !newValue.isNullOrBlank(),
                foodQuantity = newValue.orEmpty()
            )
        }
    }


    fun onUserEvent(detailsIntent: ConfirmEntryDetailsIntent) {
        viewModelScope.launch {
            when (detailsIntent) {
                None, Delete -> {}
                DismissDate -> {
                    _uiData = _uiData.copy(showDateDialog = false)
                }

                DismissTime -> {
                    _uiData = _uiData.copy(showTimeDialog = false)
                }

                OpenDate -> {
                    _uiData = _uiData.copy(showDateDialog = true)
                }

                OpenTime -> {
                    _uiData = _uiData.copy(showTimeDialog = true)
                }

                Submit -> insertNewEntry(_uiData.entity, _uiData.foodQuantity)
                is UpdateDate -> {
                    detailsIntent.newTimeInMilliSec?.let {

                        val date = fromEpochDays(it.milliseconds.inWholeDays)
                        _uiData = _uiData.copy(
                            showDateDialog = false,
                            date = date
                        )
                    }
                }

                is UpdateTime -> {
                    _uiData = _uiData.copy(
                        time = LocalTime(detailsIntent.hour, detailsIntent.min),
                        showTimeDialog = false
                    )
                }
            }

            uiState.emit(Success(_uiData))
        }
    }

    private fun insertNewEntry(foodEntity: FoodEntity, foodQuantity: String) {
        viewModelScope.launch {
            uiState.emit(Loading)
            entryState.emit(false)
            val quantity = foodQuantity.toInt()
            if (foodEntity.singleServingSizeInGm != quantity) {
                val entity = foodEntity.copy(singleServingSizeInGm = quantity)
                entity.id = foodEntity.id
                addEntry(entity)
            } else {
                addEntry(foodEntity)
            }
            entryState.emit(true)
        }
    }
}
