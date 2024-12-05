package com.example.coral.data.local

import androidx.room.TypeConverter
import com.example.coral.domain.ActivityType

class Converters {
    @TypeConverter
    fun fromActivityType(value: ActivityType): String = value.name

    @TypeConverter
    fun toActivityType(value: String): ActivityType = ActivityType.valueOf(value)
}