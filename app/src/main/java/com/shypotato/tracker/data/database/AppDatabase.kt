package com.shypotato.tracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shypotato.tracker.data.helpers.LocalDateConverters
import com.shypotato.tracker.data.helpers.LocalTimeConverters
import com.shypotato.tracker.model.entity.EntryEntity
import com.shypotato.tracker.model.entity.FoodEntity

@Database(
    version = 1,
    entities = [FoodEntity::class, EntryEntity::class],
    autoMigrations = [

    ]
)
@TypeConverters(
    LocalDateConverters::class,
    LocalTimeConverters::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getAppDao(): AppDao

}
