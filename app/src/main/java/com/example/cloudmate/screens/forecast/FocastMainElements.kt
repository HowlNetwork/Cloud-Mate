package com.example.cloudmate.screens.forecast

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudmate.R
import com.example.cloudmate.network.weatherapi.Day
import com.example.cloudmate.network.weatherapi.ForecastDay
import com.example.cloudmate.network.weatherapi.ForecastWeather
import com.example.cloudmate.network.weatherapi.Hour
import com.example.cloudmate.network.weatherapi.Weather
import com.example.cloudmate.ui.theme.poppinsFamily
import com.example.cloudmate.utils.getCurrentDate
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastMainElements() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.forecast_report),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                "Today",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = poppinsFamily
            )
        }
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
            Text(
                getCurrentDate(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                fontFamily = poppinsFamily
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourlyForecastData(data: Weather) {
    LazyRow {
        itemsIndexed(items = data.forecast.forecastday[0].hour) { index, item: Hour ->
            val weatherIconId = item.condition.code
            val dayOrNight = item.is_day
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
            val instant = Instant.ofEpochSecond(item.time_epoch)
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val time = instant.atZone(ZoneId.of("UTC")).format(formatter)
            HourlyCard(
                image = image,
                time = time,
                temperature = item.temp_c.toInt().toString() + "°C"
            )

        }
    }
}

@Composable
fun HourlyCard(image: Int, time: String, temperature: String) {
    var color = colors.primaryVariant
    var tapped by remember {
        mutableStateOf(false)
    }
    if (tapped) {
        color = colors.secondary
    }
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(80.dp)
            .padding(start = 15.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { tapped = true }
                )
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.6f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(id = image), contentDescription = stringResource(R.string.weather_icon))

            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = time, fontSize = 16.sp, fontFamily = poppinsFamily, color = Color.White)
                Text(
                    text = "$temperature°C",
                    fontSize = 18.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

        }
    }
}

@Composable
fun NextForecast() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 20.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                stringResource(R.string.next_forecast),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = poppinsFamily
            )
        }
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
            Icon(
                imageVector = Icons.Outlined.CalendarMonth,
                contentDescription = "Calendar",
                tint = Color(0xFFd68118)
            )
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyForecastData(data: Weather) {
    LazyColumn(modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
        itemsIndexed(items = data.forecast.forecastday) { index, item: ForecastDay ->
            val weatherIconId = item.day.condition.code
            val dayOrNight = item.hour[0].is_day
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
            val unixTime = item.date_epoch
            val instant = Instant.ofEpochSecond(unixTime)
            val zonedDateTime = instant.atZone(ZoneId.of("UTC"))
            val dayOfWeek = zonedDateTime.dayOfWeek.name.lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            val month = zonedDateTime.month.name.lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            val date = zonedDateTime.dayOfMonth
            val monthDate = "$month, $date"
            DailyCard(
                day = dayOfWeek,
                date = monthDate,
                temperature = item.day.avgtemp_c.toString() + "°C",
                image = image
            )
        }
    }
}

@Composable
fun DailyCard(day: String, date: String, temperature: String, image: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(bottom = 15.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colors.primaryVariant),
        elevation = CardDefaults.cardElevation(500.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.padding(start = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = day,
                    fontSize = 16.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = date,
                    fontSize = 12.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Light,
                    color = Color.White,
                    textAlign = TextAlign.Start
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$temperature°C",
                    fontSize = 26.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
            Column(
                modifier = Modifier.padding(end = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(id = image), contentDescription = stringResource(R.string.weather_icon))

            }
        }
    }

}