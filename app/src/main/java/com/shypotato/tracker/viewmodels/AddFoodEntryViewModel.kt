package com.shypotato.tracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shypotato.tracker.data.usecase.IGetAllFoodsUseCase
import com.shypotato.tracker.model.domain.UIState
import com.shypotato.tracker.model.entity.FoodEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFoodEntryViewModel @Inject constructor(
    private val getFoods: IGetAllFoodsUseCase
) : ViewModel() {
    val entryState: StateFlow<UIState<List<FoodEntity>>>
        field = MutableStateFlow<UIState<List<FoodEntity>>>(UIState.Loading)

    fun loadData() {
        viewModelScope.launch {
            entryState.emit(UIState.Loading)
            getFoods().collectLatest {
                if (it.isEmpty()) {
                    entryState.emit(UIState.Error)
                } else {
                    entryState.emit(UIState.Success(it.sortedBy { foodItem -> foodItem.name }))
                }
            }
        }
    }
}
