package com.shypotato.tracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shypotato.tracker.data.helpers.getDateTimeNow
import com.shypotato.tracker.data.usecase.IAddBowelMovementEntryUseCase
import com.shypotato.tracker.model.domain.BowelType
import com.shypotato.tracker.model.domain.ConfirmBowelQualityIntents
import com.shypotato.tracker.model.domain.ConfirmData
import com.shypotato.tracker.model.domain.ConfirmDataType.Bowel
import com.shypotato.tracker.model.domain.ConfirmEntryDetailsIntent
import com.shypotato.tracker.model.domain.UIState
import com.shypotato.tracker.model.domain.ConfirmDataType
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
    val uiState: StateFlow<UIState<ConfirmData>>
        field = MutableStateFlow<UIState<ConfirmData>>(UIState.Loading)
    val submissionSuccessful: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    private lateinit var uiData: ConfirmData

    fun handleIntent(qualityIntents: ConfirmBowelQualityIntents) {
        when (qualityIntents) {
            is ConfirmBowelQualityIntents.HandleNewType -> {
                handlePoopType(qualityIntents.type)
            }
        }

        uiState.update {
            UIState.Success(data = uiData)
        }
    }

    private fun handlePoopType(type: BowelType) {

        val instance = getDateTimeNow()

        uiData = ConfirmData(
            type = Bowel(type),
            time = instance.time,
            date = instance.date,
            showTimeDialog = false,
            showDateDialog = false,
            canDelete = false,
            submitEnabled = true
        )
    }

    fun onUserEvent(detailsIntent: ConfirmEntryDetailsIntent) {
        viewModelScope.launch {
            when (detailsIntent) {
                ConfirmEntryDetailsIntent.None, ConfirmEntryDetailsIntent.Delete -> {}
                ConfirmEntryDetailsIntent.DismissDate -> {
                    uiData = uiData.copy(showDateDialog = false)
                }

                ConfirmEntryDetailsIntent.DismissTime -> {
                    uiData = uiData.copy(showTimeDialog = false)
                }

                ConfirmEntryDetailsIntent.OpenDate -> {
                    uiData = uiData.copy(showDateDialog = true)
                }

                ConfirmEntryDetailsIntent.OpenTime -> {
                    uiData = uiData.copy(showTimeDialog = true)
                }

                ConfirmEntryDetailsIntent.Submit -> {
                    uiState.value = UIState.Loading

                    addBowel(
                        time = uiData.time,
                        date = uiData.date,
                        bowelType = (uiData.type as ConfirmDataType.Bowel).type
                    )
                    submissionSuccessful.value = true
                }

                is ConfirmEntryDetailsIntent.UpdateDate -> {
                    detailsIntent.newTimeInMilliSec?.let {
                        val date = fromEpochDays(it.milliseconds.inWholeDays)

                        uiData = uiData.copy(
                            date = date,
                            showDateDialog = false
                        )
                    }
                }

                is ConfirmEntryDetailsIntent.UpdateTime -> {
                    val time = LocalTime(detailsIntent.hour, detailsIntent.min)
                    uiData = uiData.copy(
                        showTimeDialog = false,
                        time = time
                    )
                }
            }

            uiState.update {
                UIState.Success(data = uiData)
            }
        }
    }
}

