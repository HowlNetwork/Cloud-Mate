package com.example.cloudmate.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cloudmate.module.CurrentWeatherObject
import com.example.cloudmate.module.Favourite
import com.example.cloudmate.network.weatherapi.CurrentWeather

@Database(entities = [Favourite::class, CurrentWeatherObject::class], version = 2, exportSchema = false)
abstract class CloudMateDatabase : RoomDatabase(){
    abstract fun cloudMateDao(): CloudMateDao
}