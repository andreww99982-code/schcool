package com.schcool.trainer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [PhoneEntity::class, ScenarioEntity::class, AttemptEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun phoneDao(): PhoneDao
    abstract fun scenarioDao(): ScenarioDao
    abstract fun attemptDao(): AttemptDao
}
