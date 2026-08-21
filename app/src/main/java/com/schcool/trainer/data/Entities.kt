package com.schcool.trainer.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.schcool.trainer.domain.Difficulty

@Entity(tableName = "phones")
data class PhoneEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val brand: String,
    val model: String,
    val priceRub: Int,
    val os: String,
    val memory: String,
    val camera: String,
    val battery: String,
    val screen: String,
    val connectivity: String,
    val advantages: String,
    val limitations: String
)

@Entity(tableName = "scenarios")
data class ScenarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val customerType: String,
    val difficulty: Difficulty,
    val objections: String,
    val expectedApproach: String,
    val rubricHints: String
)

@Entity(tableName = "attempts")
data class AttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val scenarioId: Long,
    val role: String,
    val score: Int,
    val passed: Boolean,
    val summary: String,
    val createdAt: Long
)
