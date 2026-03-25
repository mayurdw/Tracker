package com.mayurdw.fibretracker.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mayurdw.fibretracker.data.helpers.LocalDateConverters
import com.mayurdw.fibretracker.data.helpers.LocalTimeConverters
import com.mayurdw.fibretracker.model.entity.FoodEntity
import com.mayurdw.fibretracker.model.entity.FoodEntryEntity
import com.mayurdw.fibretracker.model.entity.PoopEntity

@Database(
    version = 2,
    entities = [FoodEntity::class, FoodEntryEntity::class, PoopEntity::class],
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(LocalDateConverters::class, LocalTimeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getAppDao(): AppDao

}
