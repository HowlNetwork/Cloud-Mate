package com.example.cloudmate.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.cloudmate.R
import com.example.cloudmate.module.CurrentWeatherObject
import com.example.cloudmate.network.common.AppResponse
import com.example.cloudmate.network.weatherapi.Weather
import com.example.cloudmate.screens.forecast.ForecastViewModel
import com.example.cloudmate.screens.home.HomeViewModel
import com.example.cloudmate.ui.theme.White
import com.example.cloudmate.ui.theme.poppinsFamily
import com.example.cloudmate.utils.RequestLocationPermission
import com.example.cloudmate.utils.createLocationRequest
import com.example.cloudmate.utils.fetchLastLocation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

@ExperimentalPermissionsApi
@Composable
fun GetCurrentLocation(
    homeViewModel: HomeViewModel,
    forecastViewModel: ForecastViewModel,
    context: Context,
    latitude: MutableState<Double>,
    longitude: MutableState<Double>,
) {
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)


    var locationFromGps: Location? by remember { mutableStateOf(null) }
    var openDialog: String by remember { mutableStateOf("") }

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    val context = LocalContext.current
    val fusedLocationProviderClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.d(
                    "onLocationResult",
                    "locationResult.latitude: ${locationResult.lastLocation?.latitude}"
                )
                locationFromGps = locationResult.lastLocation
            }
        }
    }


    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    context.fetchLastLocation(
                        fusedLocationClient = fusedLocationProviderClient,
                        settingsLauncher = null,
                        location = {
                            Log.d("settingsLauncher", "location: ${it.latitude}")
                            if (locationFromGps == null && locationFromGps != it) {
                                locationFromGps = it
                            }
                        },
                        locationCallback = locationCallback
                    )
                }

                Activity.RESULT_CANCELED -> {
                    Toast.makeText(context, "Activity.RESULT_CANCELED", Toast.LENGTH_LONG).show()
                }
            }
        }
    )

    LaunchedEffect(
        key1 = locationPermissionsState.revokedPermissions.size,
        key2 = locationPermissionsState.shouldShowRationale,
        block = {
            fetchLocation(
                locationPermissionsState,
                context,
                settingsLauncher,
                fusedLocationProviderClient,
                locationCallback,
                openDialog = {
                    openDialog = it
                })
        })

    LaunchedEffect(
        key1 = locationFromGps,
        block = {
            Log.d("LaunchedEffect", "locationFromGps: $locationFromGps")
            // TODO: setup GeoCoder

        }
    )

    DisposableEffect(
        key1 = true
    ) {
        onDispose {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    RequestLocationPermission(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    ) {

        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                val locationResult = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                )

                locationResult.addOnSuccessListener { location: Location? ->
                    // Get location. In some rare situations this can be null.
                    if (location != null) {
                        latitude.value = location.latitude
                        longitude.value = location.longitude
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error Fetching Location", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Please Enable Location", Toast.LENGTH_LONG).show()
        }

    }
    ShowData(
        homeViewModel = homeViewModel,
        forecastViewModel = forecastViewModel,
        latitude = latitude.value,
        longitude = longitude.value
    )
}


@Composable
fun ShowData(
    homeViewModel: HomeViewModel,
    forecastViewModel: ForecastViewModel,
    latitude: Double,
    longitude: Double
) {
    val gson = Gson()
    if (latitude != 360.0 && longitude != 360.0) {
        // Latitude and longitude are valid, so continue as normal
        val weatherData = produceState<AppResponse<Weather, Boolean, Exception>>(
            initialValue = AppResponse(success = true)
        ) {
            Log.d("Lat $ Lon", "$latitude and $longitude")
            value = homeViewModel.getCurrentWeather(latitude.toFloat(), longitude.toFloat())
        }.value
        weatherData.data?.current?.condition?.text?.let { Log.d("Weather", it) }
        when {
            weatherData.success == true && weatherData.data != null -> {
                forecastViewModel.insertCurrentWeatherObject(
                    CurrentWeatherObject(
                        id = 1,
                        gson.toJson(weatherData.data!!)
                    )
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val weatherIconId = weatherData.data!!.current.condition.code
                    val dayOrNight = weatherData.data!!.current.is_day
                    var image = R.drawable.sun_cloudy
                    val sunnyWeatherIds = listOf(1000)
                    val cloudyWeatherIds = listOf(1003, 1006, 1009)
                    val rainyWeatherIds =
                        listOf(1150, 1153, 1180, 1183, 1186, 1189, 1192, 1195, 1240, 1243, 1246)
                    val rainySunnyWeatherIds = listOf(1180, 1183)
                    val thunderLightningWeatherIds = listOf(1273, 1276, 1279, 1282)
                    val snowWeatherIds = listOf(
                        1066,
                        1210,
                        1213,
                        1216,
                        1219,
                        1222,
                        1114,
                        1117,
                        1204,
                        1207,
                        1255,
                        1225,
                        1258,
                        1249,
                        1252
                    )
                    val fogWeatherIds = listOf(1030, 1135, 1147)

                    image = when {
                        weatherIconId in sunnyWeatherIds && dayOrNight == 1 -> R.drawable.sunny
                        weatherIconId in cloudyWeatherIds -> R.drawable.cloudy
                        weatherIconId in rainyWeatherIds -> R.drawable.rainy
                        weatherIconId in rainySunnyWeatherIds && dayOrNight == 1 -> R.drawable.rainy_sunny
                        weatherIconId in thunderLightningWeatherIds -> R.drawable.thunder_lightning
                        weatherIconId in snowWeatherIds -> R.drawable.snow
                        weatherIconId in fogWeatherIds -> R.drawable.fog
                        weatherIconId in sunnyWeatherIds && dayOrNight == 0 -> R.drawable.clear
                        else -> R.drawable.cloudy
                    }

                    Box(
                        modifier = Modifier.background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    colors.secondary,
                                    colors.primary, Color.Transparent,
                                ), tileMode = TileMode.Clamp
                            ), alpha = 0.7F
                        )
                    ) {
                        Image(
                            painter = painterResource(id = image),
                            contentDescription = "WeatherIcon",
                            modifier = Modifier
                                .scale(
                                    1F
                                )
                        )

                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            weatherData.data!!.current.condition.text.split(' ')
                                .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = poppinsFamily,
                            color = White
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = colors.onPrimary,
                                        fontSize = 50.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = poppinsFamily
                                    )
                                ) {
                                    append(weatherData.data!!.current.temp_c.toString() + " C ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xFFd68118),
                                        fontSize = 45.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = poppinsFamily
                                    )
                                ) {
                                    append("°")
                                }
                            })
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, end = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.pressure),
                                contentDescription = "PressureIcon",
                                modifier = Modifier
                                    .scale(
                                        1F
                                    )
                                    .size(80.dp)
                            )
                            Text(
                                weatherData.data!!.current.pressure_mb.toString() + "hPa",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                fontFamily = poppinsFamily,
                                color = White
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                "Pressure",
                                fontSize = 12.sp,
                                fontFamily = poppinsFamily,
                                color = White
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.wind),
                                contentDescription = "WindIcon",
                                modifier = Modifier
                                    .scale(
                                        1F
                                    )
                                    .size(80.dp)

                            )
                            Text(
                                weatherData.data!!.current.wind_kph.toString() + "kph",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                fontFamily = poppinsFamily,
                                color = White
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text("Wind",
                                fontSize = 12.sp,
                                fontFamily = poppinsFamily,
                                color = White
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.humidity),
                                contentDescription = "HumidityIcon",
                                modifier = Modifier
                                    .scale(
                                        1F
                                    )
                                    .size(80.dp)
                            )
                            Text(
                                weatherData.data!!.current.humidity.toString() + "%",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                fontFamily = poppinsFamily,
                                color = White
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text("Humidity",
                                fontSize = 12.sp,
                                fontFamily = poppinsFamily,
                                color = White
                            )
                        }
                    }
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
}

suspend fun getLocationName(
    context: Context,
    latitude: MutableState<Double>,
    longitude: MutableState<Double>
): String {
    // To specify that the geocoding operation should be performed on the IO dispatcher
    return withContext(Dispatchers.IO) {
        /*
        withContext function will automatically suspend the current coroutine and resume it
        when the operation is complete, allowing other operations to be performed in the meantime
         */
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude.value, longitude.value, 1)
        var locationName = ""
        if (addresses != null && addresses.size > 0) {
            val address: Address = addresses[0]
            locationName = address.adminArea
        }
        locationName
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun fetchLocation(
    locationPermissionsState: MultiplePermissionsState,
    context: Context,
    settingsLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    fusedLocationProviderClient: FusedLocationProviderClient,
    locationCallback: LocationCallback,
    openDialog: (String) -> Unit
) {
    when {
        locationPermissionsState.revokedPermissions.size <= 1 -> {
            // Has permission at least one permission [coarse or fine]
            context.createLocationRequest(
                settingsLauncher = settingsLauncher,
                fusedLocationClient = fusedLocationProviderClient,
                locationCallback = locationCallback
            )
            Log.d("LaunchedEffect", "revokedPermissions.size <= 1")
        }

        locationPermissionsState.shouldShowRationale -> {
            openDialog("Should show rationale")
            Log.d("LaunchedEffect", "shouldShowRationale")
        }

        locationPermissionsState.revokedPermissions.size == 2 -> {
            locationPermissionsState.launchMultiplePermissionRequest()
            Log.d("LaunchedEffect", "revokedPermissions.size == 2")
        }

        else -> {
            openDialog("This app requires location permission")
            Log.d("LaunchedEffect", "else")
        }
    }
}
