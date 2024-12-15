package com.example.cloudmate.contracts

import com.example.cloudmate.network.common.AppResponse
import com.example.cloudmate.network.weatherapi.Weather

interface IHomeScreenViewModel {
    suspend fun getCurrentWeather(lat: Float, lon: Float): AppResponse<Weather, Boolean, Exception>
    suspend fun getForecastWeather(lat: Float, lon: Float): AppResponse<Weather, Boolean, Exception>
    fun setLatLon(lat: Double, lon: Double)
    fun getLatLon(): Pair<Double, Double>
}