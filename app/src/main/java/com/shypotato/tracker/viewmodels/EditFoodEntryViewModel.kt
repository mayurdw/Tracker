package com.shypotato.tracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shypotato.tracker.data.usecase.IDeleteEntryUseCase
import com.shypotato.tracker.data.usecase.IGetEntryUseCase
import com.shypotato.tracker.data.usecase.IUpdateEntryUseCase
import com.shypotato.tracker.model.domain.ConfirmData
import com.shypotato.tracker.model.domain.ConfirmDataType
import com.shypotato.tracker.model.domain.ConfirmEntryDetailsIntent
import com.shypotato.tracker.model.domain.Entry
import com.shypotato.tracker.model.domain.UIState
import com.shypotato.tracker.model.entity.FoodEntity
import com.shypotato.tracker.model.domain.EntryType
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
        field = MutableStateFlow<UIState<ConfirmData>>(UIState.Loading)

    private lateinit var _uiData: ConfirmData
    private lateinit var entry: Entry

    val saveSuccessful: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    fun getEntryData(selectedEntryId: Int) {
        viewModelScope.launch {
            uiState.emit(UIState.Loading)
            val entry = getEntry(selectedEntryId)

            this@EditFoodEntryViewModel.entry = entry

            when (entry.info) {
                is EntryType.Food -> {
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

                is EntryType.Bowel -> {
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

            uiState.emit(UIState.Success(_uiData))
        }
    }

    private fun updateEntry(newData: ConfirmData, entry: Entry) {

        when (newData.type) {
            is ConfirmDataType.Food -> {
                val newFoodQuantity = newData.type.foodQuantity
                val info = entry.info as EntryType.Food
                if (newFoodQuantity.toInt() != info.servingInGms || newData.time != entry.time || newData.date != entry.date) {
                    val newInfo = info.copy(servingInGms = newFoodQuantity.toInt())
                    viewModelScope.launch {
                        updateEntry(
                            entry = entry.copy(
                                time = newData.time,
                                date = newData.date,
                                info = newInfo
                            )
                        )

                        saveSuccessful.emit(true)
                    }

                }
            }

            is ConfirmDataType.Bowel -> {
                val info = entry.info as EntryType.Bowel

                if (newData.type.type != info.quality || newData.time != entry.time || newData.date != entry.time) {
                    val newInfo = info.copy(quality = newData.type.type)

                    viewModelScope.launch {
                        updateEntry(
                            entry = entry.copy(
                                time = newData.time,
                                date = newData.date,
                                info = newInfo
                            )
                        )

                        saveSuccessful.emit(true)
                    }
                }
            }
        }

    }

    fun isEdited(newValue: String?) {
        viewModelScope.launch {
            if ((entry.info as EntryType.Food).servingInGms != newValue?.trim()?.toIntOrNull()) {
                _uiData = _uiData.copy(
                    type = (_uiData.type as ConfirmDataType.Food).copy(foodQuantity = newValue.toString()),
                    submitEnabled = true
                )
            } else {
                _uiData = _uiData.copy(submitEnabled = false)
            }

            uiState.emit(UIState.Success(_uiData))

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
                ConfirmEntryDetailsIntent.None -> {}
                ConfirmEntryDetailsIntent.Delete -> {
                    delete(entry)
                }

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

                ConfirmEntryDetailsIntent.Submit -> {
                    updateEntry(
                        _uiData,
                        entry
                    )
                }

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
}
