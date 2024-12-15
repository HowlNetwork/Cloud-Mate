package com.example.cloudmate.screens.forecast

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cloudmate.contracts.IForecastScreenViewModel
import com.example.cloudmate.contracts.IHomeScreenViewModel
import com.example.cloudmate.network.common.AppResponse
import com.example.cloudmate.network.weatherapi.Weather
import com.example.cloudmate.screens.home.HomeViewModel
import com.example.cloudmate.ui.theme.poppinsFamily
import com.example.cloudmate.widgets.NavBar
import com.google.gson.Gson

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastScreen(
    navController: NavController,
    forecastViewModel: IForecastScreenViewModel,
    homeViewModel: IHomeScreenViewModel,

    ) {
//    val gson = Gson()
//    val currentWeatherObjectsList = forecastViewModel.weatherObjectList.collectAsState().value
//    Log.d("CURRENT WEATHER OBJECT LIST", "$currentWeatherObjectsList")
//    val currentWeatherObject = currentWeatherObjectsList[0]
//    Log.d("WEATHER", currentWeatherObject.weather)
//    val weatherData = gson.fromJson(currentWeatherObject.weather, Weather::class.java)
    val latitude = homeViewModel.getLatLon().first
    val longitude = homeViewModel.getLatLon().second
    val forecastWeatherData = produceState<AppResponse<Weather, Boolean, Exception>>(
        initialValue = AppResponse(success = false, data = null, e = null)
    ) {
        value = try {
            homeViewModel.getForecastWeather(
                latitude.toFloat(),
                longitude.toFloat()
            )
        } catch (e: Exception) {
            AppResponse(success = false, data = null, e = e)
        }
    }.value
    val gradientColors = listOf(Color(0xFF060620), colors.primary)
    when {
        forecastWeatherData.success == true && forecastWeatherData.data != null -> {
            Log.d("FORECAST WEATHER DATA", "${forecastWeatherData.data}")
            Box(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                Scaffold(content = { padding ->
                    Column(
                        modifier = Modifier
                            .padding(padding),
                    ) {
                        ForecastMainElements()
                        HourlyForecastData(
                            data = forecastWeatherData.data!!
                        )
                        NextForecast()
                        DailyForecastData(data = forecastWeatherData.data!!)
                    }
                }, bottomBar = {
                    NavBar(navController)
                }, containerColor = Color.Transparent)
            }
        }
        else -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color(0xFFd68118))
                Spacer(modifier = Modifier.height(10.dp))
                androidx.compose.material3.Text(
                    "Loading weather information...",
                    color = Color.White,
                    fontFamily = poppinsFamily
                )
            }
        }
    }
}
