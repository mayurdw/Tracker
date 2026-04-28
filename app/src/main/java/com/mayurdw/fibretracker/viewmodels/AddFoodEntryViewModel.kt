package com.mayurdw.fibretracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayurdw.fibretracker.data.usecase.IGetAllFoodsUseCase
import com.mayurdw.fibretracker.model.entity.FoodEntity
import com.mayurdw.fibretracker.viewmodels.UIState.Error
import com.mayurdw.fibretracker.viewmodels.UIState.Loading
import com.mayurdw.fibretracker.viewmodels.UIState.Success
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
        field = MutableStateFlow<UIState<List<FoodEntity>>>(Loading)

    fun loadData() {
        viewModelScope.launch {
            entryState.emit(Loading)
            getFoods().collectLatest {
                if (it.isEmpty()) {
                    entryState.emit(Error)
                } else {
                    entryState.emit(Success(it.sortedBy { foodItem -> foodItem.name }))
                }
            }
        }
    }
}
