package com.mayurdw.fibretracker.viewmodels

import androidx.lifecycle.ViewModel
import com.mayurdw.fibretracker.model.domain.PoopType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar
import javax.inject.Inject

sealed interface ConfirmQualityIntents {
    data class HandleNewType(val type: PoopType) : ConfirmQualityIntents
    data class HandleUpdatedTime(val hour: Int, val min: Int) : ConfirmQualityIntents
    data class HandleUpdatedDate(val newTimeInMilliSec: Long?) : ConfirmQualityIntents
    data object HandleDateDismissed : ConfirmQualityIntents
    data object HandleDateOpened : ConfirmQualityIntents
    data object HandleTimeDismissed : ConfirmQualityIntents
    data object HandleTimeOpened : ConfirmQualityIntents
    data object HandleSubmission : ConfirmQualityIntents
}

@HiltViewModel
class ConfirmPoopQualityViewModel @Inject constructor() : ViewModel() {
    val uiState: StateFlow<UIState<ConfirmPoopQualityUiData>>
        field = MutableStateFlow<UIState<ConfirmPoopQualityUiData>>(UIState.Loading)
    private lateinit var uiData: ConfirmPoopQualityUiData

    fun handleIntent(qualityIntents: ConfirmQualityIntents) {
        when (qualityIntents) {
            ConfirmQualityIntents.HandleDateDismissed -> {
                uiData = uiData.copy(showDateDialog = false)
            }

            ConfirmQualityIntents.HandleDateOpened -> {
                uiData = uiData.copy(showDateDialog = true)
            }

            ConfirmQualityIntents.HandleSubmission -> {
                uiState.update { UIState.Loading }
                return
            }

            ConfirmQualityIntents.HandleTimeDismissed -> {
                uiData = uiData.copy(showTimeDialog = false)
            }

            ConfirmQualityIntents.HandleTimeOpened -> {
                uiData = uiData.copy(
                    showTimeDialog = true
                )
            }

            is ConfirmQualityIntents.HandleUpdatedDate -> {
                uiData = uiData.copy(
                    dateInMilliSec = qualityIntents.newTimeInMilliSec ?: uiData.dateInMilliSec,
                    showDateDialog = false
                )
            }

            is ConfirmQualityIntents.HandleUpdatedTime -> {
                uiData = uiData.copy(
                    showTimeDialog = false,
                    hour = qualityIntents.hour,
                    min = qualityIntents.min
                )
            }

            is ConfirmQualityIntents.HandleNewType -> {
                handlePoopType(qualityIntents.type)
            }
        }

        uiState.update {
            UIState.Success(data = uiData)
        }
    }

    fun handlePoopType(type: PoopType) {
        uiData = ConfirmPoopQualityUiData(
            type = type,
            formattedTime = "11.30 am",
            formattedDate = "11/04/26",
            showTimeDialog = false,
            hour = 11,
            min = 30,
            showDateDialog = false,
            dateInMilliSec = Calendar.getInstance().timeInMillis
        )
    }
}

data class ConfirmPoopQualityUiData(
    val type: PoopType,
    val formattedDate: String,
    val formattedTime: String,
    val showTimeDialog: Boolean,
    val showDateDialog: Boolean,
    val hour: Int,
    val min: Int,
    val dateInMilliSec: Long,
)