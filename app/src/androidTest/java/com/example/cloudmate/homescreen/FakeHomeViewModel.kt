package com.example.cloudmate.homescreen

import androidx.compose.runtime.mutableStateOf
import com.example.cloudmate.network.weatherapi.Weather
import androidx.lifecycle.ViewModel
import com.example.cloudmate.contracts.IHomeScreenViewModel
import com.example.cloudmate.network.common.AppResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeHomeViewModel : IHomeScreenViewModel, ViewModel() {

    // Mô phỏng tọa độ
    private var latitude = mutableStateOf(360.0)
    private var longitude = mutableStateOf(360.0)

    // StateFlow giả để giữ dữ liệu thời tiết hiện tại và dự báo
    private val _currentWeatherFlow = MutableStateFlow<AppResponse<Weather, Boolean, Exception>>(
        AppResponse(success = false, data = null, e = null)
    )
    val currentWeatherFlow: StateFlow<AppResponse<Weather, Boolean, Exception>> = _currentWeatherFlow

    private val _forecastWeatherFlow = MutableStateFlow<AppResponse<Weather, Boolean, Exception>>(
        AppResponse(success = false, data = null, e = null)
    )
    val forecastWeatherFlow: StateFlow<AppResponse<Weather, Boolean, Exception>> = _forecastWeatherFlow

    // Hàm để giả lập lấy dữ liệu thời tiết hiện tại
    override suspend fun getCurrentWeather(
        lat: Float, lon: Float
    ): AppResponse<Weather, Boolean, Exception> {
        return _currentWeatherFlow.value
    }

    // Hàm để giả lập lấy dữ liệu dự báo thời tiết
    override suspend fun getForecastWeather(
        lat: Float, lon: Float
    ): AppResponse<Weather, Boolean, Exception> {
        return _forecastWeatherFlow.value
    }

    // Hàm cập nhật dữ liệu giả cho thời tiết hiện tại
    fun updateCurrentWeather(weather: AppResponse<Weather, Boolean, Exception>) {
        _currentWeatherFlow.value = weather
    }

    // Hàm cập nhật dữ liệu giả cho dự báo thời tiết
    fun updateForecastWeather(weather: AppResponse<Weather, Boolean, Exception>) {
        _forecastWeatherFlow.value = weather
    }

    // Mô phỏng thiết lập tọa độ
    override fun setLatLon(lat: Double, lon: Double) {
        latitude.value = lat
        longitude.value = lon
    }

    // Mô phỏng lấy tọa độ
    override fun getLatLon(): Pair<Double, Double> {
        return Pair(latitude.value, longitude.value)
    }
}
