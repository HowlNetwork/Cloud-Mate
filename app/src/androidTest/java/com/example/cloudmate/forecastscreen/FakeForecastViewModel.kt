package com.example.cloudmate.forecastscreen

import com.example.cloudmate.contracts.IForecastScreenViewModel
import com.example.cloudmate.module.CurrentWeatherObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeForecastViewModel : IForecastScreenViewModel {
    private val _weatherObjectList = MutableStateFlow<List<CurrentWeatherObject>>(emptyList())
    override val weatherObjectList: StateFlow<List<CurrentWeatherObject>> = _weatherObjectList

    private val weatherObjects = mutableListOf<CurrentWeatherObject>()

    override suspend fun getWeatherById(id: Int): CurrentWeatherObject {
        return weatherObjects.find { it.id == id }
            ?: throw IllegalArgumentException("Weather object not found")
    }

    override fun insertCurrentWeatherObject(currentWeatherObject: CurrentWeatherObject) {
        weatherObjects.add(currentWeatherObject)
        _weatherObjectList.value = weatherObjects.toList()
    }

    override fun updateCurrentWeatherObject(currentWeatherObject: CurrentWeatherObject) {
        val index = weatherObjects.indexOfFirst { it.id == currentWeatherObject.id }
        if (index != -1) {
            weatherObjects[index] = currentWeatherObject
            _weatherObjectList.value = weatherObjects.toList()
        }
    }
}