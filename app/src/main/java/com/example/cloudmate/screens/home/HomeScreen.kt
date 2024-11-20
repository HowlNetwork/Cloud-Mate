package com.example.cloudmate.screens.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.cloudmate.screens.forecast.ForecastViewModel
import com.example.cloudmate.widgets.NavBar
import kotlinx.coroutines.runBlocking

@Composable
fun HomeScreen(
    navController: NavController,
    city: String?,
    context: Context,
    homeViewModel: HomeViewModel,
    forecastViewModel: ForecastViewModel
) {
    Log.d("City", "$city")
    lateinit var latitude: MutableState<Double>
    lateinit var longitude: MutableState<Double>

    if (city == "default") {
        latitude = remember {
            mutableStateOf(360.0)
        }
        longitude = remember {
            mutableStateOf(360.0)
        }
    } else {
        val address = city?.let { getLatLon(context, it) }
        if (address != null) {
            latitude = remember {
                mutableStateOf(address.latitude)
            }
            longitude = remember {
                mutableStateOf(address.longitude)
            }
        } else {
            latitude = remember {
                mutableStateOf(360.0)
            }
            longitude = remember {
                mutableStateOf(360.0)
            }
            Toast.makeText(context, "Unknown Location", Toast.LENGTH_LONG).show()

        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            content = { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                ) {
                    HomeScreenElements(
                        context = context,
                        latitude = latitude,
                        longitude = longitude,
                        homeViewModel = homeViewModel,
                        forecastViewModel = forecastViewModel
                    )
                }
            },
            bottomBar = {
                NavBar(navController = navController)
            },
            containerColor = Color.Transparent
        )
    }
}
