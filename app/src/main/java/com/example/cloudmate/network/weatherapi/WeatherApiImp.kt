package com.example.cloudmate.network.weatherapi

import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class WeatherApiImp @Inject constructor(private val api: WeatherApi) {
    suspend fun getCurrentWeather(): List<Weather> {
        return withContext(Dispatchers.IO){
            val response = api.getCurrentWeather("", "")
            response.body() ?: emptyList()
        }
    }
}