package com.example.cloudmate.module

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_tbl")
data class CurrentWeatherObject(
    @PrimaryKey
    val id: Int = 0,

    @ColumnInfo(name = "weather")
    val weather: String,
)