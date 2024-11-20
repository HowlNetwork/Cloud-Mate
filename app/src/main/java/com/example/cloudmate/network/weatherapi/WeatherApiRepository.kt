package com.example.cloudmate.network.weatherapi

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.cloudmate.network.common.AppResponse
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class WeatherApiRepository @Inject constructor(private val api: WeatherApi) {
    suspend fun getCurrentWeather(
        lat: Float,
        lon: Float,
        aqi: Boolean = false
    ): AppResponse<Weather, Boolean, Exception> {
        val response = try {
            api.getCurrentWeather("${lat},${lon}", if (aqi) "yes" else "no")
        } catch (e: Exception) {
            return AppResponse(data = null, success = false, e = e)
        }

        val currentWeather: Weather = response.body() ?: return AppResponse(data = null, success = false, e = null)
        return AppResponse(data = currentWeather, success = true, e = null)
    }

    suspend fun getForecastWeather(
        lat: Float,
        lon: Float,
        days: Int = 7,
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
                "${lat},${lon}",
                days,
                if (aqi) "yes" else "no",
                if (alert) "yes" else "no"
            )
        } catch (e: Exception) {
            return AppResponse(data = null, success = false, e = e)
        }

        var currentWeather: Weather? =
            response.body() ?: return AppResponse(data = null, success = false, e = null)
        return AppResponse(data = currentWeather, success = true, e = null)
    }
}