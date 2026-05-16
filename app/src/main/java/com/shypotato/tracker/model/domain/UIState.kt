package com.shypotato.tracker.model.domain

sealed interface UIState<out T> {
    data object Error : UIState<Nothing>
    data object Loading : UIState<Nothing>
    data class Success<T>(val data: T) : UIState<T>
}