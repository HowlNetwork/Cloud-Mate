package com.example.cloudmate.network.weatherapi

import com.example.cloudmate.network.common.Alerts
import com.example.cloudmate.network.common.Location

data class Weather(
    val location: Location,
    val current: CurrentWeather,
    val forecast: ForecastWeather,
    val alerts: Alerts
)