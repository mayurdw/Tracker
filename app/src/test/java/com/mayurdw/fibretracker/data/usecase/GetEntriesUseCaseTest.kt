package com.mayurdw.fibretracker.data.usecase

import app.cash.turbine.test
import com.mayurdw.fibretracker.TestDispatcherRule
import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.data.helpers.getDateToday
import com.mayurdw.fibretracker.model.domain.BowelQuality.TYPE_3
import com.mayurdw.fibretracker.model.domain.EntryType.Bowel
import com.mayurdw.fibretracker.model.domain.EntryType.Food
import com.mayurdw.fibretracker.model.entity.EntityType.BOWEL_MOVEMENT
import com.mayurdw.fibretracker.model.entity.EntityType.FOOD
import com.mayurdw.fibretracker.model.entity.EntryEntity
import com.mayurdw.fibretracker.model.entity.FoodEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetEntriesUseCaseTest {
    @MockK
    lateinit var dao: AppDao

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    lateinit var useCase: GetEntriesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this, true)

        useCase = GetEntriesUseCase(
            coroutineDispatcher = Dispatchers.Unconfined,
            dao = dao
        )
    }

    @Test
    fun testEmptyDatabaseReturnsEmptyList() = runTest {
        coEvery {
            dao.getEntries(
                ofType(LocalDate::class),
                ofType(LocalDate::class)
            )
        } returns flow { emit(emptyList()) }

        useCase(getDateToday(), getDateToday()).test {
            assertTrue(awaitItem().isEmpty())
            awaitComplete()
        }
    }

    @Test
    fun testDatabaseWithOnlyFoodEntity() = runTest {
        val foodEntity = mockk<FoodEntity>(relaxed = true)
        val entryEntity = mockk<EntryEntity>(relaxed = true)


        coEvery { entryEntity.foodId } returns 1
        coEvery { foodEntity.name } returns "Name"
        coEvery { entryEntity.type } returns FOOD
        coEvery {
            dao.getEntries(
                any(),
                any()
            )
        } returns flow {
            emit(listOf(entryEntity))
        }
        coEvery {
            dao.getFoodById(any())
        } returns foodEntity

        useCase(getDateToday(), getDateToday()).test {
            val entries = awaitItem()

            assertTrue(entries.isNotEmpty())
            assertEquals(1, entries.size)
            assertTrue(entries[0].info is Food)
            assertEquals(foodEntity.name, (entries[0].info as Food).name)
            awaitComplete()
        }
    }

    @Test
    fun testDatabaseWithFoodAndBowelEntity() = runTest {
        val foodEntity = mockk<FoodEntity>(relaxed = true)
        val foodEntry = mockk<EntryEntity>(relaxed = true)
        val bowelEntry = mockk<EntryEntity>(relaxed = true)

        coEvery { foodEntry.foodId } returns 1
        coEvery { foodEntity.name } returns "Name"
        coEvery { foodEntry.type } returns FOOD
        coEvery { bowelEntry.type } returns BOWEL_MOVEMENT
        coEvery { bowelEntry.quality } returns TYPE_3
        coEvery {
            dao.getEntries(
                any(),
                any()
            )
        } returns flow {
            emit(listOf(foodEntry, bowelEntry))
        }
        coEvery {
            dao.getFoodById(any())
        } returns foodEntity

        useCase(getDateToday(), getDateToday()).test {
            val entries = awaitItem()

            assertTrue(entries.isNotEmpty())
            assertEquals(2, entries.size)
            assertTrue(entries[0].info is Food)
            assertTrue(entries[1].info is Bowel)
            assertEquals(foodEntity.name, (entries[0].info as Food).name)
            assertEquals(bowelEntry.quality, (entries[1].info as Bowel).quality)

            awaitComplete()
        }
    }

    @Test
    fun testIncorrectlyConfiguredFoodEntry() = runTest {
        val foodEntry = mockk<EntryEntity>(relaxed = true)

        coEvery { foodEntry.foodId } returns 1
        coEvery { foodEntry.type } returns FOOD
        coEvery {
            dao.getEntries(
                any(),
                any()
            )
        } returns flow {
            emit(listOf(foodEntry))
        }
        coEvery {
            dao.getFoodById(any())
        } returns null

        useCase(getDateToday(), getDateToday()).test {
            assertTrue(awaitError() is NullPointerException)
        }
    }
}