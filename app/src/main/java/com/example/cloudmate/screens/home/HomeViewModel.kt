package com.example.cloudmate.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cloudmate.contracts.IHomeScreenViewModel
import com.example.cloudmate.network.common.AppResponse
import com.example.cloudmate.network.weatherapi.Weather
import com.example.cloudmate.network.weatherapi.WeatherApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: WeatherApiRepository) : ViewModel(),
    IHomeScreenViewModel {
    private var latitude = mutableStateOf(360.0)
    private var longitude = mutableStateOf(360.0)
    override suspend fun getCurrentWeather(
        lat: Float, lon: Float
    ): AppResponse<Weather, Boolean, Exception> {
        return repository.getCurrentWeather(lat, lon)
    }

    override suspend fun getForecastWeather(
        lat: Float, lon: Float
    ): AppResponse<Weather, Boolean, Exception> {
        return repository.getForecastWeather(lat, lon)
    }

    override fun setLatLon(lat: Double, lon: Double) {
        latitude.value = lat
        longitude.value = lon
    }

    override fun getLatLon(): Pair<Double, Double> {
        return Pair(latitude.value, longitude.value)
    }
}

