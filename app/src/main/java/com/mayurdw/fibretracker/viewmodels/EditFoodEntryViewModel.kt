package com.mayurdw.fibretracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayurdw.fibretracker.data.usecase.IDeleteEntryUseCase
import com.mayurdw.fibretracker.data.usecase.IGetEntryUseCase
import com.mayurdw.fibretracker.data.usecase.IUpdateEntryUseCase
import com.mayurdw.fibretracker.model.domain.ConfirmData
import com.mayurdw.fibretracker.model.domain.ConfirmDataType
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
import com.mayurdw.fibretracker.model.domain.Entry
import com.mayurdw.fibretracker.model.domain.EntryType.Bowel
import com.mayurdw.fibretracker.model.domain.EntryType.Food
import com.mayurdw.fibretracker.model.domain.UIState
import com.mayurdw.fibretracker.model.domain.UIState.Loading
import com.mayurdw.fibretracker.model.domain.UIState.Success
import com.mayurdw.fibretracker.model.entity.FoodEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate.Companion.fromEpochDays
import kotlinx.datetime.LocalTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class EditFoodEntryViewModel @Inject constructor(
    private val getEntry: IGetEntryUseCase,
    private val updateEntry: IUpdateEntryUseCase,
    private val deleteEntry: IDeleteEntryUseCase
) : ViewModel() {
    val uiState: StateFlow<UIState<ConfirmData>>
        field = MutableStateFlow<UIState<ConfirmData>>(Loading)

    private lateinit var _uiData: ConfirmData
    private lateinit var entry: Entry

    val saveSuccessful: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    fun getEntryData(selectedEntryId: Int) {
        viewModelScope.launch {
            uiState.emit(Loading)
            val entry = getEntry(selectedEntryId)

            this@EditFoodEntryViewModel.entry = entry

            when (entry.info) {
                is Food -> {
                    _uiData = ConfirmData(
                        time = entry.time,
                        date = entry.date,
                        showTimeDialog = false,
                        showDateDialog = false,
                        canDelete = true,
                        submitEnabled = false,
                        type = ConfirmDataType.Food(
                            entity = FoodEntity(
                                entry.info.name,
                                entry.info.servingInGms,
                                entry.info.fibrePerMicroGrams
                            )
                        )
                    )

                }

                is Bowel -> {
                    _uiData = ConfirmData(
                        time = entry.time,
                        date = entry.date,
                        showTimeDialog = false,
                        showDateDialog = false,
                        submitEnabled = false,
                        type = ConfirmDataType.Bowel(
                            entry.info.quality
                        ),
                        canDelete = true
                    )
                }
            }

            uiState.emit(Success(_uiData))
        }
    }

    private fun updateEntry(newFoodQuantity: String, entry: Entry) {
        val info = entry.info as Food
        if (newFoodQuantity.toInt() != info.servingInGms) {
            val newInfo = info.copy(servingInGms = newFoodQuantity.toInt())
            viewModelScope.launch {
                updateEntry(
                    entry = entry.copy(
                        info = newInfo
                    )
                )

                saveSuccessful.emit(true)
            }

        }
    }

    fun isEdited(newValue: String?) {
        viewModelScope.launch {
            if ((entry.info as Food).servingInGms != newValue?.trim()?.toIntOrNull()) {
                _uiData = _uiData.copy(
                    type = (_uiData.type as ConfirmDataType.Food).copy(foodQuantity = newValue.toString()),
                    submitEnabled = true
                )
            } else {
                _uiData = _uiData.copy(submitEnabled = false)
            }

            uiState.emit(Success(_uiData))

        }
    }

    private fun delete(entry: Entry) {
        viewModelScope.launch {
            deleteEntry(entry)
            saveSuccessful.emit(true)
        }
    }

    fun onUserEvent(detailsIntent: ConfirmEntryDetailsIntent) {
        viewModelScope.launch {
            when (detailsIntent) {
                None -> {}
                Delete -> {
                    delete(entry)
                }

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

                Submit -> {
                    updateEntry(
                        (_uiData.type as ConfirmDataType.Food).foodQuantity,
                        entry
                    )
                }

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
}
