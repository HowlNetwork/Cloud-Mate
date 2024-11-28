package com.example.cloudmate.network.weatherapi

import android.annotation.SuppressLint
import com.example.cloudmate.network.common.AppResponse
import javax.inject.Inject

class WeatherApiRepository @Inject constructor(private val api: WeatherApi) {
    @SuppressLint("DefaultLocale")
    private fun formatCoordinates(lat: Float, lon: Float): String {
        return String.format("%.6f,%.6f", lat, lon)
    }

    private fun formatBooleanParam(value: Boolean): String {
        return if (value) "yes" else "no"
    }


    @SuppressLint("DefaultLocale")
    suspend fun getCurrentWeather(
        lat: Float,
        lon: Float,
        aqi: Boolean = false
    ): AppResponse<Weather, Boolean, Exception> {
        val response = try {
            api.getCurrentWeather(
                formatCoordinates(lat,lon),
                formatBooleanParam(aqi)
            )
        } catch (e: Exception) {
            return AppResponse(data = null, success = false, e = e)
        }

        val currentWeather: Weather = response.body() ?: return AppResponse(data = null, success = false, e = null)
        return AppResponse(data = currentWeather, success = true, e = null)
    }



    @SuppressLint("DefaultLocale")
    suspend fun getForecastWeather(
        lat: Float,
        lon: Float,
        days: Int = 1,
        aqi: Boolean = false,
        alert: Boolean = false
    ): AppResponse<Weather, Boolean, Exception> {
        if (days < 1 || days > 10) {
            return AppResponse(
                data = null,
                success = true,
                e = Exception("Days params smaller than 1 or bigger than 10")
            )
        }

        val response = try {
            api.getForecastWeather(
                formatCoordinates(lat, lon),
                days,
                formatBooleanParam(aqi),
                formatBooleanParam(alert)
            )
        } catch (e: Exception) {
            return AppResponse(data = null, success = false, e = e)
        }

        val currentWeather: Weather = response.body() ?: return AppResponse(data = null, success = false, e = null)
        return AppResponse(data = currentWeather, success = true, e = null)
    }
}