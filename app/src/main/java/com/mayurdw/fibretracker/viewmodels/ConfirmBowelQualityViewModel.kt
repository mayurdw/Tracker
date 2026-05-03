package com.mayurdw.fibretracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayurdw.fibretracker.data.helpers.getDateTimeNow
import com.mayurdw.fibretracker.data.helpers.getFormattedDate
import com.mayurdw.fibretracker.data.helpers.getFormattedTime
import com.mayurdw.fibretracker.data.usecase.IAddBowelMovementEntryUseCase
import com.mayurdw.fibretracker.model.domain.BowelType
import com.mayurdw.fibretracker.model.entity.EntityType.BOWEL_MOVEMENT
import com.mayurdw.fibretracker.model.entity.EntryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.days
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
    private val addPoop: IAddBowelMovementEntryUseCase
) : ViewModel() {
    val uiState: StateFlow<UIState<ConfirmBowelQualityData>>
        field = MutableStateFlow<UIState<ConfirmBowelQualityData>>(UIState.Loading)
    val submissionSuccessful: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    private lateinit var uiData: ConfirmBowelQualityData

    fun handleIntent(qualityIntents: ConfirmQualityIntents) {
        when (qualityIntents) {
            ConfirmQualityIntents.HandleDateDismissed -> {
                uiData = uiData.copy(showDateDialog = false)
            }

            ConfirmQualityIntents.HandleDateOpened -> {
                uiData = uiData.copy(showDateDialog = true)
            }

            ConfirmQualityIntents.HandleSubmission -> {
                viewModelScope.launch {
                    uiState.value = UIState.Loading
                    val entity = EntryEntity(
                        type = BOWEL_MOVEMENT,
                        time = LocalTime(uiData.hour, uiData.min),
                        date = LocalDate.fromEpochDays(uiData.dateInMilliSec.milliseconds.inWholeDays),
                        quality = uiData.type
                    )

                    // TODO: Change this
//                    addPoop(entity)
                    submissionSuccessful.value = true
                }
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
                        formattedDate = date.getFormattedDate(),
                        showDateDialog = false
                    )
                }
            }

            is ConfirmQualityIntents.HandleUpdatedTime -> {
                val time = LocalTime(qualityIntents.hour, qualityIntents.min)
                uiData = uiData.copy(
                    showTimeDialog = false,
                    formattedTime = time.getFormattedTime(),
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

    private fun handlePoopType(type: BowelType) {

        val instance = getDateTimeNow()

        uiData = ConfirmBowelQualityData(
            type = type,
            formattedTime = instance.time.getFormattedTime(),
            formattedDate = instance.date.getFormattedDate(),
            showTimeDialog = false,
            hour = instance.time.hour,
            min = instance.time.minute,
            showDateDialog = false,
            dateInMilliSec = instance.date.toEpochDays().days.inWholeMilliseconds
        )
    }
}

data class ConfirmBowelQualityData(
    val type: BowelType,
    val formattedDate: String,
    val formattedTime: String,
    val showTimeDialog: Boolean,
    val showDateDialog: Boolean,
    val hour: Int,
    val min: Int,
    val dateInMilliSec: Long,
)