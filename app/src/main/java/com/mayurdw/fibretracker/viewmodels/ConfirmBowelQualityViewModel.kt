package com.mayurdw.fibretracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayurdw.fibretracker.data.helpers.getDateTimeNow
import com.mayurdw.fibretracker.data.usecase.IAddBowelMovementEntryUseCase
import com.mayurdw.fibretracker.model.domain.BowelType
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleDateDismissed
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleDateOpened
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleNewType
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleSubmission
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleTimeDismissed
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleTimeOpened
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleUpdatedDate
import com.mayurdw.fibretracker.viewmodels.ConfirmQualityIntents.HandleUpdatedTime
import com.mayurdw.fibretracker.viewmodels.UIState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDate.Companion.fromEpochDays
import kotlinx.datetime.LocalTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

sealed interface ConfirmQualityIntents {
    data class HandleNewType(val type: BowelType) : ConfirmQualityIntents
    data class HandleUpdatedTime(val hour: Int, val min: Int) : ConfirmQualityIntents
    data class HandleUpdatedDate(val newTimeInMilliSec: Long?) : ConfirmQualityIntents
    data object HandleDateDismissed : ConfirmQualityIntents
    data object HandleDateOpened : ConfirmQualityIntents
    data object HandleTimeDismissed : ConfirmQualityIntents
    data object HandleTimeOpened : ConfirmQualityIntents
    data object HandleSubmission : ConfirmQualityIntents
}

@HiltViewModel
class ConfirmBowelQualityViewModel @Inject constructor(
    private val addBowel: IAddBowelMovementEntryUseCase
) : ViewModel() {
    val uiState: StateFlow<UIState<ConfirmBowelQualityData>>
        field = MutableStateFlow<UIState<ConfirmBowelQualityData>>(Loading)
    val submissionSuccessful: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    private lateinit var uiData: ConfirmBowelQualityData

    fun handleIntent(qualityIntents: ConfirmQualityIntents) {
        when (qualityIntents) {
            HandleDateDismissed -> {
                uiData = uiData.copy(showDateDialog = false)
            }

            HandleDateOpened -> {
                uiData = uiData.copy(showDateDialog = true)
            }

            HandleSubmission -> {
                viewModelScope.launch {
                    uiState.value = Loading

                    addBowel(
                        time = uiData.time,
                        date = uiData.date,
                        bowelType = uiData.type
                    )
                    submissionSuccessful.value = true
                }
            }

            HandleTimeDismissed -> {
                uiData = uiData.copy(showTimeDialog = false)
            }

            HandleTimeOpened -> {
                uiData = uiData.copy(
                    showTimeDialog = true
                )
            }

            is HandleUpdatedDate -> {
                qualityIntents.newTimeInMilliSec?.let {
                    val date = fromEpochDays(it.milliseconds.inWholeDays)

                    uiData = uiData.copy(
                        date = date,
                        showDateDialog = false
                    )
                }
            }

            is HandleUpdatedTime -> {
                val time = LocalTime(qualityIntents.hour, qualityIntents.min)
                uiData = uiData.copy(
                    showTimeDialog = false,
                    time = time
                )
            }

            is HandleNewType -> {
                handlePoopType(qualityIntents.type)
            }
        }

        uiState.update {
            UIState.Success(data = uiData)
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
}

data class ConfirmBowelQualityData(
    val type: BowelType,
    val time: LocalTime,
    val date: LocalDate,
    val showTimeDialog: Boolean,
    val showDateDialog: Boolean
)