package com.mayurdw.fibretracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayurdw.fibretracker.data.usecase.IDeleteEntryUseCase
import com.mayurdw.fibretracker.data.usecase.IGetEntryUseCase
import com.mayurdw.fibretracker.data.usecase.IUpdateEntryUseCase
import com.mayurdw.fibretracker.model.domain.EntryData
import com.mayurdw.fibretracker.model.entity.FoodEntryEntity
import com.mayurdw.fibretracker.viewmodels.UIState.Error
import com.mayurdw.fibretracker.viewmodels.UIState.Loading
import com.mayurdw.fibretracker.viewmodels.UIState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditFoodEntryViewModel @Inject constructor(
    private val getEntry: IGetEntryUseCase,
    private val updateEntry: IUpdateEntryUseCase,
    private val deleteEntry: IDeleteEntryUseCase
) : ViewModel() {

    val selectedEntryFlow: StateFlow<UIState<EntryData>>
        field = MutableStateFlow<UIState<EntryData>>(Loading)

    val saveSuccessful: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    fun getEntryData(selectedEntryId: Int) {
        viewModelScope.launch {
            val entry = getEntry(selectedEntryId).firstOrNull()

            entry?.let {
                selectedEntryFlow.emit(Success(entry))
            } ?: run {
                selectedEntryFlow.emit(Error)
            }
        }
    }

    fun updateEntry(newFoodQuantity: String, entry: EntryData) {
        if (newFoodQuantity.toInt() != entry.servingInGms) {

            viewModelScope.launch {
                updateEntry(
                    FoodEntryEntity(
                        foodId = entry.foodId,
                        foodServingInGms = newFoodQuantity.toInt(),
                        date = entry.date
                    ).apply { id = entry.id }
                )

                saveSuccessful.emit(true)
            }

        }
    }

    fun isEdited(newValue: String?, entry: EntryData): Boolean {
        if (newValue.isNullOrBlank() && newValue?.toIntOrNull() == null)
            return false

        return entry.servingInGms != newValue.toInt()
    }

    fun delete(entry: EntryData) {
        viewModelScope.launch {
            deleteEntry(entry)
            saveSuccessful.emit(true)
        }
    }
}
