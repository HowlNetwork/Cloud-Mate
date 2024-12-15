package com.example.cloudmate.screens.home

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudmate.R
import com.example.cloudmate.contracts.IForecastScreenViewModel
import com.example.cloudmate.contracts.IHomeScreenViewModel
import com.example.cloudmate.location.GetCurrentLocation
import com.example.cloudmate.location.getLocationName
import com.example.cloudmate.screens.forecast.ForecastViewModel
import com.example.cloudmate.ui.theme.White
import com.example.cloudmate.ui.theme.poppinsFamily
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreenElements(
    homeViewModel: IHomeScreenViewModel,
    forecastViewModel: IForecastScreenViewModel,
    context: Context,
    latitude: MutableState<Double>,
    longitude: MutableState<Double>,
) {

    var locationName by remember {
        mutableStateOf("")
    }
    // cancelled when the composition is disposed
    val scope = rememberCoroutineScope()
    if (latitude.value != 360.0 && longitude.value != 360.0) {
        LaunchedEffect(latitude, longitude) {
            scope.launch {
                locationName = try {
                    getLocationName(context, latitude, longitude)
                } catch (e: Exception) {
                    ""
                }
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Outlined.LocationOn,
            contentDescription = stringResource(R.string.location_icon),
            tint = colors.secondary
        )
        Text(
            locationName,
            fontSize = 16.sp,
            fontFamily = poppinsFamily,
            color = White,
            modifier = Modifier.testTag("CityName")
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp), horizontalArrangement = Arrangement.Start
    ) {
        Text(
            stringResource(R.string.today_report),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily,
            color = White
        )
    }
    GetCurrentLocation(
        homeViewModel = homeViewModel,
        forecastViewModel = forecastViewModel,
        context = context,
        latitude = latitude,
        longitude = longitude
    )
}

fun getLatLon(context: Context, cityName: String): Address? {
    val geocoder = Geocoder(context)
    return try {
        val addresses = geocoder.getFromLocationName(cityName, 1)
        addresses!![0]
    } catch (e: Exception) {
        // Toast.makeText(context, "Unknown Location", Toast.LENGTH_SHORT).show()
        null
    }
}