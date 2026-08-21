package com.schcool.trainer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhoneDao {
    @Query("SELECT * FROM phones ORDER BY brand, model")
    fun observePhones(): Flow<List<PhoneEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PhoneEntity>)

    @Query("SELECT COUNT(*) FROM phones")
    suspend fun count(): Int
}

@Dao
interface ScenarioDao {
    @Query("SELECT * FROM scenarios ORDER BY difficulty, id")
    fun observeScenarios(): Flow<List<ScenarioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ScenarioEntity>)

    @Query("SELECT COUNT(*) FROM scenarios")
    suspend fun count(): Int
}

@Dao
interface AttemptDao {
    @Query("SELECT * FROM attempts ORDER BY createdAt DESC")
    fun observeAttempts(): Flow<List<AttemptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AttemptEntity)
}
