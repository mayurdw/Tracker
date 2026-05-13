package com.mayurdw.fibretracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayurdw.fibretracker.data.helpers.getCurrentDate
import com.mayurdw.fibretracker.data.helpers.getCurrentTime
import com.mayurdw.fibretracker.data.usecase.IAddFoodEntryUseCase
import com.mayurdw.fibretracker.data.usecase.IGetFoodUseCase
import com.mayurdw.fibretracker.model.domain.ConfirmData
import com.mayurdw.fibretracker.model.domain.ConfirmDataType.Food
import com.mayurdw.fibretracker.model.domain.ConfirmEntryDetailsIntent
import com.mayurdw.fibretracker.model.domain.ConfirmEntryDetailsIntent.Delete
import com.mayurdw.fibretracker.model.domain.ConfirmEntryDetailsIntent.DismissDate
import com.mayurdw.fibretracker.model.domain.ConfirmEntryDetailsIntent.DismissTime
import com.mayurdw.fibretracker.model.domain.ConfirmEntryDetailsIntent.None
import com.mayurdw.fibretracker.model.domain.ConfirmEntryDetailsIntent.OpenDate
import com.mayurdw.fibretracker.model.domain.ConfirmEntryDetailsIntent.OpenTime
import com.mayurdw.fibretracker.model.domain.ConfirmEntryDetailsIntent.Submit
import com.mayurdw.fibretracker.model.domain.ConfirmEntryDetailsIntent.UpdateDate
import com.mayurdw.fibretracker.model.domain.ConfirmEntryDetailsIntent.UpdateTime
import com.mayurdw.fibretracker.model.domain.UIState
import com.mayurdw.fibretracker.model.domain.UIState.Error
import com.mayurdw.fibretracker.model.domain.UIState.Loading
import com.mayurdw.fibretracker.model.domain.UIState.Success
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
        field = MutableStateFlow<UIState<ConfirmData>>(Loading)
    private lateinit var _uiData: ConfirmData

    val entryState: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    fun loadFoodDetails(id: Int) {
        viewModelScope.launch {
            uiState.emit(Loading)
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
                type = (_uiData.type as Food).copy(foodQuantity = newValue.orEmpty()),
            )

            uiState.emit(Success(_uiData))
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

                Submit -> insertNewEntry(_uiData)
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

    private fun insertNewEntry(uiData: ConfirmData) {
        viewModelScope.launch {
            uiState.emit(Loading)
            entryState.emit(false)

            val quantity = (uiData.type as Food).foodQuantity.toInt()

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
