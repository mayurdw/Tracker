package com.shypotato.tracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shypotato.tracker.data.helpers.getCurrentDate
import com.shypotato.tracker.data.helpers.getCurrentTime
import com.shypotato.tracker.data.usecase.IAddFoodEntryUseCase
import com.shypotato.tracker.data.usecase.IGetFoodUseCase
import com.shypotato.tracker.model.domain.ConfirmData
import com.shypotato.tracker.model.domain.ConfirmDataType.Food
import com.shypotato.tracker.model.domain.ConfirmEntryDetailsIntent
import com.shypotato.tracker.model.domain.UIState
import com.shypotato.tracker.model.domain.ConfirmDataType
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
    private val addEntry: IAddFoodEntryUseCase
) : ViewModel() {
    val uiState: StateFlow<UIState<ConfirmData>>
        field = MutableStateFlow<UIState<ConfirmData>>(UIState.Loading)
    private lateinit var _uiData: ConfirmData

    val entryState: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    fun loadFoodDetails(id: Int) {
        viewModelScope.launch {
            uiState.emit(UIState.Loading)
            getFood(id).collectLatest {
                if (it.isSuccess) {
                    _uiData = ConfirmData(
                        time = getCurrentTime(),
                        date = getCurrentDate(),
                        showTimeDialog = false,
                        showDateDialog = false,
                        canDelete = false,
                        submitEnabled = false,
                        type = Food(
                            entity = it.getOrNull()!!
                        )
                    )
                    uiState.emit(UIState.Success(_uiData))
                } else {
                    uiState.emit(UIState.Error)
                }
            }
        }
    }

    fun handleQuantityChange(newValue: String?) {
        viewModelScope.launch {
            _uiData = _uiData.copy(
                submitEnabled = !newValue.isNullOrBlank(),
                type = (_uiData.type as ConfirmDataType.Food).copy(foodQuantity = newValue.orEmpty()),
            )

            uiState.emit(UIState.Success(_uiData))
        }
    }


    fun onUserEvent(detailsIntent: ConfirmEntryDetailsIntent) {
        viewModelScope.launch {
            when (detailsIntent) {
                ConfirmEntryDetailsIntent.None, ConfirmEntryDetailsIntent.Delete -> {}
                ConfirmEntryDetailsIntent.DismissDate -> {
                    _uiData = _uiData.copy(showDateDialog = false)
                }

                ConfirmEntryDetailsIntent.DismissTime -> {
                    _uiData = _uiData.copy(showTimeDialog = false)
                }

                ConfirmEntryDetailsIntent.OpenDate -> {
                    _uiData = _uiData.copy(showDateDialog = true)
                }

                ConfirmEntryDetailsIntent.OpenTime -> {
                    _uiData = _uiData.copy(showTimeDialog = true)
                }

                ConfirmEntryDetailsIntent.Submit -> insertNewEntry(_uiData)
                is ConfirmEntryDetailsIntent.UpdateDate -> {
                    detailsIntent.newTimeInMilliSec?.let {

                        val date = fromEpochDays(it.milliseconds.inWholeDays)
                        _uiData = _uiData.copy(
                            showDateDialog = false,
                            date = date
                        )
                    }
                }

                is ConfirmEntryDetailsIntent.UpdateTime -> {
                    _uiData = _uiData.copy(
                        time = LocalTime(detailsIntent.hour, detailsIntent.min),
                        showTimeDialog = false
                    )
                }
            }

            uiState.emit(UIState.Success(_uiData))
        }
    }

    private fun insertNewEntry(uiData: ConfirmData) {
        viewModelScope.launch {
            uiState.emit(UIState.Loading)
            entryState.emit(false)

            val quantity = (uiData.type as ConfirmDataType.Food).foodQuantity.toInt()

            if (uiData.type.entity.singleServingSizeInGm != quantity) {
                val entity = uiData.type.entity.copy(singleServingSizeInGm = quantity)
                entity.id = uiData.type.entity.id
                addEntry(entity, uiData.time, uiData.date)
            } else {
                addEntry(uiData.type.entity, uiData.time, uiData.date)
            }
            entryState.emit(true)
        }
    }
}
