package com.mayurdw.fibretracker.viewmodels

import androidx.lifecycle.ViewModel
import com.mayurdw.fibretracker.model.domain.PoopType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds

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
    val submissionSuccessful: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

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
                submissionSuccessful.update { true }
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
                qualityIntents.newTimeInMilliSec?.let {
                    val date = LocalDate.fromEpochDays(it.milliseconds.inWholeDays)

                    uiData = uiData.copy(
                        dateInMilliSec = it,
                        formattedDate = getFormattedDate(date),
                        showDateDialog = false
                    )
                }
            }

            is ConfirmQualityIntents.HandleUpdatedTime -> {
                val time = LocalTime(qualityIntents.hour, qualityIntents.min)
                uiData = uiData.copy(
                    showTimeDialog = false,
                    formattedTime = getFormattedTime(time),
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

    private fun getCurrentTime(): LocalDateTime {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    private fun getFormattedTime(time: LocalTime): String {
        val timeFormat = LocalTime.Format {
            amPmHour()
            char(':')
            minute()
            char(' ')
            amPmMarker("am", "pm")
        }

        return time.format(timeFormat)
    }

    private fun getFormattedDate(date: LocalDate): String {
        val dateFormat = LocalDate.Format {
            day()
            char('/')
            monthNumber()
            char('/')
            yearTwoDigits(2000)
        }

        return date.format(dateFormat)
    }

    private fun handlePoopType(type: PoopType) {

        val instance = getCurrentTime()

        uiData = ConfirmPoopQualityUiData(
            type = type,
            formattedTime = getFormattedTime(instance.time),
            formattedDate = getFormattedDate(instance.date),
            showTimeDialog = false,
            hour = instance.time.hour,
            min = instance.time.minute,
            showDateDialog = false,
            dateInMilliSec = instance.date.toEpochDays().days.inWholeMilliseconds
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