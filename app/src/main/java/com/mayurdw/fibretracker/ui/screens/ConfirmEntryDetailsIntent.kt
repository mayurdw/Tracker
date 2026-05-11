package com.mayurdw.fibretracker.ui.screens

sealed interface ConfirmEntryDetailsIntent {
    data object None: ConfirmEntryDetailsIntent

    data object Delete: ConfirmEntryDetailsIntent

    data object Submit: ConfirmEntryDetailsIntent

    data object OpenDate: ConfirmEntryDetailsIntent

    data object DismissDate: ConfirmEntryDetailsIntent

    data object OpenTime: ConfirmEntryDetailsIntent

    data object DismissTime: ConfirmEntryDetailsIntent

    data class UpdateTime( val hour: Int, val min: Int): ConfirmEntryDetailsIntent

    data class UpdateDate(val newTimeInMilliSec: Long?): ConfirmEntryDetailsIntent
}