package com.schcool.trainer.data

import androidx.room.TypeConverter
import com.schcool.trainer.domain.Difficulty

class Converters {
    @TypeConverter
    fun fromDifficulty(value: Difficulty): String = value.name

    @TypeConverter
    fun toDifficulty(value: String): Difficulty = Difficulty.valueOf(value)
}
