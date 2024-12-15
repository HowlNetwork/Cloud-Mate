package com.example.cloudmate.contracts

import com.example.cloudmate.module.CurrentWeatherObject
import kotlinx.coroutines.flow.StateFlow

interface IForecastScreenViewModel {
    val weatherObjectList: StateFlow<List<CurrentWeatherObject>>
    suspend fun getWeatherById(id: Int): CurrentWeatherObject
    fun insertCurrentWeatherObject(currentWeatherObject: CurrentWeatherObject)
    fun updateCurrentWeatherObject(currentWeatherObject: CurrentWeatherObject)
}