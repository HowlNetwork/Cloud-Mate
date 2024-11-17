package com.example.cloudmate.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cloudmate.network.common.AppResponse
import com.example.cloudmate.network.weatherapi.Weather
import com.example.cloudmate.network.weatherapi.WeatherApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: WeatherApiRepository) : ViewModel() {
    private var latitude = mutableStateOf(360.0)
    private var longitude = mutableStateOf(360.0)
    suspend fun getCurrentWeather(
        lat: Float,lon : Float
    ) : AppResponse<Weather, Boolean, Exception> {
        return repository.getCurrentWeather(lat,lon)
    }
}

