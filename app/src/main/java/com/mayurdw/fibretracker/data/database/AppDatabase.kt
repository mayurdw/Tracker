package com.mayurdw.fibretracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mayurdw.fibretracker.data.helpers.LocalDateConverters
import com.mayurdw.fibretracker.data.helpers.LocalTimeConverters
import com.mayurdw.fibretracker.model.entity.EntryEntity
import com.mayurdw.fibretracker.model.entity.FoodEntity

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
