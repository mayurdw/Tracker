package com.mayurdw.fibretracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayurdw.fibretracker.data.helpers.getDateTimeNow
import com.mayurdw.fibretracker.data.usecase.IAddBowelMovementEntryUseCase
import com.mayurdw.fibretracker.model.domain.BowelType
import com.mayurdw.fibretracker.model.domain.ConfirmBowelQualityData
import com.mayurdw.fibretracker.model.domain.ConfirmBowelQualityIntents
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
import com.mayurdw.fibretracker.model.domain.ConfirmBowelQualityIntents.HandleNewType
import com.mayurdw.fibretracker.model.domain.UIState
import com.mayurdw.fibretracker.model.domain.UIState.Loading
import com.mayurdw.fibretracker.model.domain.UIState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate.Companion.fromEpochDays
import kotlinx.datetime.LocalTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ConfirmBowelQualityViewModel @Inject constructor(
    private val addBowel: IAddBowelMovementEntryUseCase
) : ViewModel() {
    val uiState: StateFlow<UIState<ConfirmBowelQualityData>>
        field = MutableStateFlow<UIState<ConfirmBowelQualityData>>(Loading)
    val submissionSuccessful: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    private lateinit var uiData: ConfirmBowelQualityData

    fun handleIntent(qualityIntents: ConfirmBowelQualityIntents) {
        when (qualityIntents) {
            is HandleNewType -> {
                handlePoopType(qualityIntents.type)
            }
        }

        uiState.update {
            Success(data = uiData)
        }
    }

    private fun handlePoopType(type: BowelType) {

        val instance = getDateTimeNow()

        uiData = ConfirmBowelQualityData(
            type = type,
            time = instance.time,
            date = instance.date,
            showTimeDialog = false,
            showDateDialog = false,
        )
    }

    fun onUserEvent(detailsIntent: ConfirmEntryDetailsIntent) {
        viewModelScope.launch {
            when (detailsIntent) {
                None, Delete -> {}
                DismissDate -> {
                    uiData = uiData.copy(showDateDialog = false)
                }

                DismissTime -> {
                    uiData = uiData.copy(showTimeDialog = false)
                }

                OpenDate -> {
                    uiData = uiData.copy(showDateDialog = true)
                }

                OpenTime -> {
                    uiData = uiData.copy(showTimeDialog = true)
                }

                Submit -> {
                    uiState.value = Loading

                    addBowel(
                        time = uiData.time,
                        date = uiData.date,
                        bowelType = uiData.type
                    )
                    submissionSuccessful.value = true
                }

                is UpdateDate -> {
                    detailsIntent.newTimeInMilliSec?.let {
                        val date = fromEpochDays(it.milliseconds.inWholeDays)

                        uiData = uiData.copy(
                            date = date,
                            showDateDialog = false
                        )
                    }
                }

                is UpdateTime -> {
                    val time = LocalTime(detailsIntent.hour, detailsIntent.min)
                    uiData = uiData.copy(
                        showTimeDialog = false,
                        time = time
                    )
                }
            }

            uiState.update {
                Success(data = uiData)
            }
        }
    }
}

