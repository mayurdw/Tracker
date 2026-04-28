package com.mayurdw.fibretracker.viewmodels

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayurdw.fibretracker.data.usecase.IAddFoodUseCase
import com.mayurdw.fibretracker.data.usecase.IDeleteFoodUseCase
import com.mayurdw.fibretracker.data.usecase.IGetFoodUseCase
import com.mayurdw.fibretracker.model.entity.FoodEntity
import com.mayurdw.fibretracker.viewmodels.UIState.Error
import com.mayurdw.fibretracker.viewmodels.UIState.Loading
import com.mayurdw.fibretracker.viewmodels.UIState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class AddNewFoodViewModel @Inject constructor(
    private val getFood: IGetFoodUseCase,
    private val addFood: IAddFoodUseCase,
    private val deleteFood: IDeleteFoodUseCase
) : ViewModel() {
    val uiState: StateFlow<UIState<FoodEntity>>
        field = MutableStateFlow<UIState<FoodEntity>>(Loading)

    fun getFoodById(foodId: Int) {
        viewModelScope.launch {

            uiState.emit(Loading)
            getFood(foodId).collectLatest {
                if (it.isSuccess) {
                    uiState.emit(Success(it.getOrNull()!!))
                } else {
                    uiState.emit(Error)
                }
            }
        }
    }

    fun isValid(foodName: String, foodServing: String, fibrePerServing: String): Boolean {
        return foodName.isNotBlank() && foodServing.isNotBlank() && fibrePerServing.isNotBlank()
    }

    fun isUpdated(
        data: FoodEntity,
        foodName: String,
        foodServing: String,
        fibrePerServing: String
    ): Boolean {
        return foodName != data.name ||
                foodServing.isBlank() || foodServing.toInt() != data.singleServingSizeInGm ||
                fibrePerServing.isBlank() || fibrePerServing.toBigDecimal() != (data.fibrePerGram * data.singleServingSizeInGm.toBigDecimal())
    }

    fun updateFood(
        foodEntity: FoodEntity,
        newName: String,
        newServingSize: String,
        newFibrePerServing: String
    ) {
        viewModelScope.launch {
            if (isUpdated(foodEntity, newName, newServingSize, newFibrePerServing)) {
                val newEntity = foodEntity.copy(
                    name = newName,
                    singleServingSizeInGm = newServingSize.toInt(),
                    fibrePerMicroGram = getFibrePerMicroGram(
                        fibrePerServingSizeInGms = newFibrePerServing,
                        foodServingSize = newServingSize
                    )
                ).apply {
                    id = foodEntity.id
                }

                addFood(newEntity)
            }
        }
    }

    @VisibleForTesting
    suspend fun getFibrePerMicroGram(
        fibrePerServingSizeInGms: String,
        foodServingSize: String
    ): Int {
        return viewModelScope.async(Default) {
            val fibrePerServing = BigDecimal(fibrePerServingSizeInGms)
            val servingSize = BigDecimal(foodServingSize)

            ((fibrePerServing * BigDecimal(1_000_000)) / servingSize).toInt()
        }.await()
    }

    fun addNewFood(foodName: String, foodServingSize: String, fibrePerServingInGms: String) {
        viewModelScope.launch {
            if (isValid(foodName, foodServingSize, fibrePerServingInGms)) {
                try {
                    val foodEntity = FoodEntity(
                        name = foodName,
                        singleServingSizeInGm = foodServingSize.toInt(),
                        fibrePerMicroGram = getFibrePerMicroGram(
                            foodServingSize = foodServingSize,
                            fibrePerServingSizeInGms = fibrePerServingInGms
                        )
                    )
                    addFood(foodEntity)
                } catch (_: Exception) {

                }
            }
        }
    }

    fun delete(foodEntity: FoodEntity) {
        viewModelScope.launch {
            deleteFood(foodEntity)
        }
    }
}
