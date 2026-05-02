package com.mayurdw.fibretracker.data.usecase

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.mayurdw.fibretracker.TestDispatcherRule
import com.mayurdw.fibretracker.data.database.AppDao
import com.mayurdw.fibretracker.data.database.AppDatabase
import com.mayurdw.fibretracker.data.helpers.getCurrentTime
import com.mayurdw.fibretracker.data.helpers.getDateToday
import com.mayurdw.fibretracker.model.entity.EntityType
import com.mayurdw.fibretracker.model.entity.EntryEntity
import com.mayurdw.fibretracker.model.entity.FoodEntity
import com.mayurdw.fibretracker.model.entity.FoodEntryEntity
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimeUnit.Companion.DAY
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@RunWith(RobolectricTestRunner::class)
class AppDatabaseTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var db: AppDatabase
    private lateinit var dao: AppDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries()
            .build()
        dao = db.getAppDao()
    }

    @After
    fun finish() {
        db.close()
    }

    @Test
    fun testMultimapBetweenFoodAndEntry() = runTest {
        val currentDate = getCurrentDate()
        val food = FoodEntity(
            name = "Food",
            singleServingSizeInGm = 10,
            fibrePerMicroGram = 2000
        ).apply { id = 1 }
        val entry = FoodEntryEntity(
            foodId = food.id,
            foodServingInGms = 5,
            date = currentDate
        )

        dao.upsertNewFood(
            food
        )

        dao.upsertEntry(
            entry
        )

        dao.getEntryData(LocalDate.fromEpochDays(0), currentDate).test {
            val list = awaitItem()

            assertEquals(1, list.size)
            assertEquals(entry.foodServingInGms, list[0].servingInGms)
            assertEquals(food.name, list[0].name)
        }
    }

    @Test
    fun testMultipleEntriesOfSameFood() = runTest {
        val currentDate = getCurrentDate()
        val food = FoodEntity(
            name = "Test",
            singleServingSizeInGm = 10,
            fibrePerMicroGram = 2000
        ).apply { id = 1 }
        val entries = listOf(
            FoodEntryEntity(
                foodId = food.id,
                foodServingInGms = food.singleServingSizeInGm,
                date = currentDate
            ),
            FoodEntryEntity(
                foodId = food.id,
                foodServingInGms = food.singleServingSizeInGm * 2,
                date = currentDate.minus(1, DAY)
            )
        )

        entries.forEach { dao.upsertEntry(it) }
        dao.upsertNewFood(food)

        dao.getEntryData(currentDate, currentDate).test {
            val list = awaitItem()

            assertEquals(1, list.size)
            assertEquals(entries[0].foodServingInGms, list[0].servingInGms)
        }
    }

    @Test
    fun testMultipleEntriesOfSameDate() = runTest {
        val currentDate = getCurrentDate()
        val food = FoodEntity(
            name = "Test",
            singleServingSizeInGm = 10,
            fibrePerMicroGram = 2000
        ).apply { id = 1 }
        val entries = listOf(
            FoodEntryEntity(
                foodId = food.id,
                foodServingInGms = food.singleServingSizeInGm,
                date = currentDate
            ),
            FoodEntryEntity(
                foodId = food.id,
                foodServingInGms = food.singleServingSizeInGm * 2,
                date = currentDate
            )
        )

        entries.forEach { dao.upsertEntry(it) }
        dao.upsertNewFood(food)

        dao.getEntryData(currentDate, currentDate).test {
            val list = awaitItem()

            assertEquals(2, list.size)
            assertEquals(entries[0].foodServingInGms, list[0].servingInGms)
            assertEquals(entries[1].foodServingInGms, list[1].servingInGms)
        }
    }

    fun testUpsertASingleEntry() = runTest {
        val currentDate = getCurrentDate()
        val food = FoodEntity(
            name = "Test",
            singleServingSizeInGm = 10,
            fibrePerMicroGram = 2000
        ).apply { id = 1 }
        val entry =
            FoodEntryEntity(
                foodId = food.id,
                foodServingInGms = food.singleServingSizeInGm,
                date = currentDate
            ).apply { id = 1 }

        dao.upsertEntry(entry)
        dao.upsertNewFood(food)


        dao.getEntryData(currentDate, currentDate).test {
            val list = awaitItem()

            assertEquals(1, list.size)
        }


        val newEntry = entry.copy(foodServingInGms = food.singleServingSizeInGm / 2)

        assertEquals(entry.id, newEntry.id)
        assertEquals(entry.foodServingInGms, newEntry.foodServingInGms * 2)
        dao.upsertEntry(newEntry)

        dao.getEntryData(currentDate, currentDate).test {
            val list = awaitItem()

            assertEquals(1, list.size)
            assertEquals(newEntry.foodServingInGms, list[0].servingInGms)
        }
    }

    @Test
    fun testGetSingleEntry() = runTest {
        val currentDate = getCurrentDate()
        val food = FoodEntity(
            name = "Test",
            singleServingSizeInGm = 10,
            fibrePerMicroGram = 2000
        ).apply { id = 1 }
        val entry =
            FoodEntryEntity(
                foodId = food.id,
                foodServingInGms = food.singleServingSizeInGm,
                date = currentDate
            ).apply { id = 1 }

        dao.upsertEntry(entry)
        dao.upsertNewFood(food)


        dao.getEntry(entry.id).test {
            val data = awaitItem()

            assertEquals(entry.id, data.id)
            assertEquals(entry.foodServingInGms, data.servingInGms)
            assertEquals(entry.date, data.date)
        }
    }

    @Test
    fun testUpdateFood() = runTest {
        val food = FoodEntity(
            name = "Test",
            singleServingSizeInGm = 10,
            fibrePerMicroGram = 2000
        )

        dao.upsertNewFood(food)

        dao.getAllFoods().test {
            val list = awaitItem()

            val updatedFood =
                list[0].copy(fibrePerMicroGram = food.fibrePerMicroGram / 2)
                    .apply { id = list[0].id }

            dao.upsertNewFood(updatedFood)

            val newList = awaitItem()

            assertEquals(1, list.size)
            assertEquals(food.name, list[0].name)
            assertNotEquals(updatedFood.fibrePerMicroGram, food.fibrePerMicroGram)
            assertEquals(1, newList.size)
            assertEquals(food.name, newList[0].name)
            assertEquals(updatedFood.fibrePerMicroGram, newList[0].fibrePerMicroGram)
        }
    }

    @Test
    fun getFoodIfNotMatched() = runTest {
        assertNull(dao.getFoodById(-1))
    }

    @Test
    fun deleteEntryIfNoneExist() = runTest {
        dao.deleteEntry(mockk<FoodEntryEntity>(relaxed = true))
        dao.deleteFood(mockk<FoodEntity>(relaxed = true))
    }

    @Test
    fun deleteEntry() = runTest {
        val currentDate = getCurrentDate()
        val food = FoodEntity(
            name = "Test",
            singleServingSizeInGm = 10,
            fibrePerMicroGram = 2000
        ).apply { id = 1 }
        val entry =
            FoodEntryEntity(
                foodId = food.id,
                foodServingInGms = food.singleServingSizeInGm,
                date = currentDate
            ).apply { id = 1 }

        dao.upsertEntry(entry)
        dao.upsertNewFood(food)

        dao.getEntryData(currentDate, currentDate).test {
            val list = awaitItem()

            assertEquals(1, list.size)
        }
        dao.deleteEntry(entry)
        dao.getEntryData(currentDate, currentDate).test {
            val list = awaitItem()

            assertTrue(list.isEmpty())
        }
    }

    @Test
    fun deleteFood() = runTest {
        val food = FoodEntity(
            name = "Test",
            singleServingSizeInGm = 10,
            fibrePerMicroGram = 2000
        ).apply { id = 1 }

        dao.upsertNewFood(food)

        dao.getAllFoods().test {
            assertEquals(1, awaitItem().size)
        }

        dao.deleteFood(food)

        dao.getAllFoods().test {
            assertTrue(awaitItem().isEmpty())
        }
    }

    @Test
    fun getEntriesWhenNoneExist() = runTest {
        dao.getEntries(getCurrentDate(), getCurrentDate()).test {
            assertTrue(awaitItem().isEmpty())
        }
    }

    @Test
    fun getEntriesWhenEntriesOlder() = runTest {
        val entry = EntryEntity(
            date = getCurrentDate().minus(1, DAY),
            time = getCurrentTime(),
            type = EntityType.BOWEL_MOVEMENT
        )

        dao.insertEntry(entry)

        dao.getEntries(getCurrentDate(), getCurrentDate()).test {
            assertTrue(awaitItem().isEmpty())
        }
    }

    @Test
    fun getEntriesTest() = runTest {
        val entry = EntryEntity(
            date = getCurrentDate(),
            time = getCurrentTime(),
            type = EntityType.FOOD
        )

        dao.insertEntry(entry)

        dao.getEntries(getCurrentDate(), getCurrentDate()).test {
            assertEquals(1, awaitItem().size)
        }
    }

    fun getCurrentDate(): LocalDate = getDateToday()
}
