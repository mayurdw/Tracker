package com.mayurdw.fibretracker.viewmodels

import android.content.res.Resources
import app.cash.turbine.test
import com.mayurdw.fibretracker.TestDispatcherRule
import com.mayurdw.fibretracker.data.usecase.IAddFoodUseCase
import com.mayurdw.fibretracker.data.usecase.IDeleteFoodUseCase
import com.mayurdw.fibretracker.data.usecase.IGetFoodUseCase
import com.mayurdw.fibretracker.model.domain.CommonFoods
import com.mayurdw.fibretracker.model.entity.FoodEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal


@OptIn(ExperimentalCoroutinesApi::class)
class AddNewFoodViewModelTest {
    @MockK
    lateinit var getFoodUseCase: IGetFoodUseCase

    @MockK
    lateinit var addFoodUseCase: IAddFoodUseCase

    @MockK
    lateinit var deleteFoodUseCase: IDeleteFoodUseCase

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    lateinit var viewModel: AddNewFoodViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, true)

        viewModel = AddNewFoodViewModel(
            getFood = getFoodUseCase,
            addFood = addFoodUseCase,
            deleteFood = deleteFoodUseCase
        )
    }

    @Test
    fun checkIfDependenciesAreNotNull() {
        assertNotNull(getFoodUseCase)
        assertNotNull(addFoodUseCase)
        assertNotNull(deleteFoodUseCase)
        assertTrue(this::addFoodUseCase.isInitialized)
        assertTrue(this::getFoodUseCase.isInitialized)
        assertTrue(this::deleteFoodUseCase.isInitialized)
    }

    @Test
    fun checkIfValidWhenValid() {
        assertTrue(
            viewModel.isValid(
                foodName = "Name",
                foodServing = "24",
                fibrePerServing = "0.2"
            )
        )
    }

    @Test
    fun checkIfValidWhenBlank() {
        assertFalse(
            viewModel.isValid(
                foodName = "Name",
                foodServing = "  ",
                fibrePerServing = "0.2"
            )
        )
    }

    @Test
    fun checkIfValidWhenInvalid() {
        assertFalse(
            viewModel.isValid(
                foodName = "",
                foodServing = "",
                fibrePerServing = "0.2"
            )
        )
    }

    @Test
    fun checkIfUpdatedWhenNotUpdated() {
        val entity = FoodEntity(name = "Test", 1, 1_000_000)
        assertFalse(
            viewModel.isUpdated(
                data = entity,
                foodName = entity.name,
                foodServing = entity.singleServingSizeInGm.toString(),
                fibrePerServing = entity.fibrePerGram.toString()
            )
        )
    }

    @Test
    fun checkIfUpdatedWhenUpdated() {
        val entity = FoodEntity(name = "Test", 1, 1_000_000)
        assertTrue(
            viewModel.isUpdated(
                data = entity,
                foodName = entity.name,
                foodServing = entity.singleServingSizeInGm.toString(),
                fibrePerServing = "2"
            )
        )
        assertTrue(
            viewModel.isUpdated(
                data = entity,
                foodName = entity.name,
                foodServing = entity.singleServingSizeInGm.toString(),
                fibrePerServing = "2.9"
            )
        )
    }

    @Test(expected = NumberFormatException::class)
    fun checkIfUpdatedWhenInvalid() {
        val entity = FoodEntity(name = "Test", 1, 1_000_000)


        viewModel.isUpdated(
            data = entity,
            foodName = entity.name,
            foodServing = "Test",
            fibrePerServing = entity.fibrePerGram.toString()
        )

    }

    @Test
    fun checkIfUpdatedWhenBlank() {
        val entity = FoodEntity(name = "Test", 1, 1_000_000)

        assertTrue(
            viewModel.isUpdated(
                data = entity,
                foodName = entity.name,
                foodServing = " ",
                fibrePerServing = entity.fibrePerGram.toString()
            )
        )

        assertTrue(
            viewModel.isUpdated(
                data = entity,
                foodName = entity.name,
                foodServing = entity.singleServingSizeInGm.toString(),
                fibrePerServing = " "
            )
        )
    }

    @Test
    fun testIfNoFoodFound() = runTest {
        val flow = flow {
            emit(Result.failure<FoodEntity>(exception = Resources.NotFoundException()))
        }
        coEvery { getFoodUseCase(ofType(Int::class)) } returns flow


        viewModel.uiState.test {
            viewModel.getFoodById(-1)
            advanceUntilIdle()

            assertEquals(awaitItem(), UIState.Loading)
            assertEquals(awaitItem(), UIState.Error)
        }
    }


    @Test
    fun testIfFoodFound() = runTest {
        val entity = FoodEntity(
            name = "Test",
            fibrePerMicroGram = 1_00_0,
            singleServingSizeInGm = 100
        )
        val flow = flow {
            emit(Result.success(entity))
        }
        coEvery { getFoodUseCase(ofType(Int::class)) } returns flow

        viewModel.uiState.test {
            viewModel.getFoodById(1)
            advanceUntilIdle()

            assertEquals(awaitItem(), UIState.Loading)
            val state = awaitItem()
            assertTrue(state is UIState.Success<FoodEntity>)
            assertEquals(entity.name, (state as UIState.Success<FoodEntity>).data.name)
        }
    }


    @Test
    fun testIfFoodUpdated() = runTest {
        val entity =
            FoodEntity(name = "Test", fibrePerMicroGram = 1_000_000, singleServingSizeInGm = 100)
        val newFibre = 200

        coEvery { addFoodUseCase(ofType(FoodEntity::class)) } returns Unit

        assertTrue(
            viewModel.isUpdated(
                entity,
                entity.name,
                entity.singleServingSizeInGm.toString(),
                newFibre.toString()
            )
        )

        viewModel.updateFood(
            entity,
            newName = entity.name,
            newServingSize = entity.singleServingSizeInGm.toString(),
            newFibrePerServing = newFibre.toString()
        )

        coVerify {
            addFoodUseCase(
                FoodEntity(
                    name = entity.name,
                    singleServingSizeInGm = entity.singleServingSizeInGm,
                    fibrePerMicroGram = 2_000_000
                )
            )
        }
    }

    @Test
    fun testUpdatedFoodComplicatedValues() = runTest {
        val entity =
            FoodEntity(name = "Test", fibrePerMicroGram = 1_950_000, singleServingSizeInGm = 100)
        val newFibre = 282

        coEvery { addFoodUseCase(ofType(FoodEntity::class)) } returns Unit

        assertTrue(
            viewModel.isUpdated(
                entity,
                entity.name,
                entity.singleServingSizeInGm.toString(),
                newFibre.toString()
            )
        )

        viewModel.updateFood(
            entity,
            newName = entity.name,
            newServingSize = entity.singleServingSizeInGm.toString(),
            newFibrePerServing = newFibre.toString()
        )

        coVerify {
            addFoodUseCase(
                FoodEntity(
                    name = entity.name,
                    singleServingSizeInGm = entity.singleServingSizeInGm,
                    fibrePerMicroGram = 2_820_000
                )
            )
        }
    }

    @Test
    fun testGetFibreValue() = runTest {
        assertEquals(
            CommonFoods[0].fibrePerMicroGram, viewModel.getFibrePerMicroGram(
                foodServingSize = CommonFoods[0].singleServingSizeInGm.toString(),
                fibrePerServingSizeInGms = (CommonFoods[0].fibrePerGram * BigDecimal(CommonFoods[0].singleServingSizeInGm)).toString()
            )
        )
    }
}
