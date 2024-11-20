package com.example.cloudmate.screens.forecast

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.cloudmate.network.common.AppResponse
import com.example.cloudmate.network.weatherapi.Weather
import com.example.cloudmate.screens.home.HomeViewModel
import com.example.cloudmate.widgets.NavBar
import com.google.gson.Gson

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastScreen(
    navController: NavController,
    forecastViewModel: ForecastViewModel,
    homeViewModel: HomeViewModel,
    context: Context
) {
    val gson = Gson()
    val currentWeatherObjectsList = forecastViewModel.weatherObjectList.collectAsState().value
    Log.d("CURRENT WEATHER OBJECT LIST", "$currentWeatherObjectsList")
    val currentWeatherObject = currentWeatherObjectsList[0]
    Log.d("WEATHER", currentWeatherObject.weather)
    val weatherData = gson.fromJson(currentWeatherObject.weather, Weather::class.java)
    val forecastWeatherData = produceState<AppResponse<Weather, Boolean, Exception>>(
        initialValue = AppResponse(success = true)
    ) {
        value = homeViewModel.getForecastWeather(weatherData.location.lat.toFloat(),
            weatherData.location.lon.toFloat())
    }.value
    val gradientColors = listOf(Color(0xFF060620), colors.primary)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                )
            )
    ) {
        Scaffold(content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding),
            ) {
                ForecastMainElements()
                HourlyForecastData(
                    data = forecastWeatherData.data!!,
                )
                NextForecast()
                DailyForecastData(data = forecastWeatherData.data!!)
            }
        }, bottomBar = {
            NavBar(navController)
        }, containerColor = Color.Transparent)
    }

}
