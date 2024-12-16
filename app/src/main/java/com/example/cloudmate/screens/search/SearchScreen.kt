import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cloudmate.R
import com.example.cloudmate.components.InputField
import com.example.cloudmate.contracts.IHomeScreenViewModel
import com.example.cloudmate.contracts.ISearchScreenViewModel
import com.example.cloudmate.module.Favourite
import com.example.cloudmate.network.common.AppResponse
import com.example.cloudmate.network.weatherapi.Weather
import com.example.cloudmate.screens.home.HomeViewModel
import com.example.cloudmate.screens.home.getLatLon
import com.example.cloudmate.screens.search.SearchScreenViewModel
import com.example.cloudmate.ui.theme.LightNavyBlue
import com.example.cloudmate.ui.theme.White
import com.example.cloudmate.ui.theme.poppinsFamily
import com.example.cloudmate.widgets.BottomNavItem
import com.example.cloudmate.widgets.NavBar

@Composable
fun SearchScreen(
    context: Context,
    navController: NavController,
    searchScreenViewModel: ISearchScreenViewModel,
    homeViewModel: IHomeScreenViewModel
) {
    var list = searchScreenViewModel.favList.collectAsState().value
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(R.string.pick_location),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFamily,
                        color = White
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(R.string.find_city),
                        fontSize = 14.sp,
                        fontFamily = poppinsFamily,
                        textAlign = TextAlign.Center,
                        color = White
                    )
                }
                SearchBar { city ->
                    val connectivityManager =
                        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                    val isConnected: Boolean = activeNetwork?.isConnected == true
                    if (isConnected) {
                        // Navigate to HomeScreen
                        navController.navigate(BottomNavItem.Home.route + "/$city")
                        val address = getLatLon(context, city)
                        val latitude = address?.latitude
                        val longitude = address?.longitude
                        if (latitude != null || longitude != null) {
                            // Insert record to database
                            searchScreenViewModel.insertFavourite(
                                Favourite(
                                    city = city,
                                    lat = latitude!!,
                                    lon = longitude!!,
                                )
                            )
                        }
                    } else {
                        Toast.makeText(context, "No internet connection!", Toast.LENGTH_LONG).show()
                    }

                }
                Log.d("TAG", "$list")
                list = list.reversed()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (list.isEmpty()) {
                        Text(
                            text = stringResource(R.string.no_favourite),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFamily,
                            textAlign = TextAlign.Center,
                            color = White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(list.size) { index ->
                                FavCard(
                                    index = index,
                                    favourite = list[index],
                                    context = context,
                                    navController = navController,
                                    homeViewModel = homeViewModel,
                                    searchScreenViewModel = searchScreenViewModel
                                )
                            }
                        }
                    }
                }

            }
        }, bottomBar = {
            NavBar(navController)
        }, containerColor = Color.Transparent)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(onSearch: (String) -> Unit = {}) {
    val searchState = rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(searchState.value) {
        searchState.value.trim().isNotEmpty()
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, start = 15.dp, end = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        InputField(
            valueState = searchState,
            labelId = "City name",
            enabled = true,
            isSingleLine = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchState.value.trim())
                keyboardController?.hide()
                searchState.value = ""
            })
    }
}

@Composable
fun FavCard(
    index: Int,
    favourite: Favourite,
    context: Context,
    navController: NavController,
    homeViewModel: IHomeScreenViewModel,
    searchScreenViewModel: ISearchScreenViewModel
) {
    var color = LightNavyBlue
    if (index == 0) {
        color = LightNavyBlue
    }
    Card(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .testTag("${favourite.city} Card")
            .clickable {
                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                val isConnected: Boolean = activeNetwork?.isConnected == true
                if (isConnected) {
                    navController.popBackStack()
                    navController.navigate(BottomNavItem.Home.route + "/${favourite.city}")
                } else {
                    Toast
                        .makeText(context, "No internet connection!", Toast.LENGTH_LONG)
                        .show()
                }
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(500.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        val weatherData = produceState<AppResponse<Weather, Boolean, Exception>>(
            initialValue = AppResponse(success = true)
        ) {
            value =
                homeViewModel.getCurrentWeather(favourite.lat.toFloat(), favourite.lon.toFloat())
        }.value
        Log.d("Weather", "${weatherData.data}")
        if (weatherData.data != null) {
            Log.d("Weather", "${weatherData.data}")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                    Text(
                        weatherData.data!!.current.temp_c.toString() + "°C",
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = White
                    )
                    Text(
                        weatherData.data!!.current.condition.text.split(' ')
                            .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = poppinsFamily,
                        color = White
                    )
                }
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

                Image(
                    painter = painterResource(id = image),
                    contentDescription = stringResource(R.string.weather_icon),
                    modifier = Modifier
                        .scale(
                            1F
                        )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                    Text(
                        favourite.city,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Medium,
                        color = White
                    )
                }
                Column(verticalArrangement = Arrangement.Bottom) {
                    Icon(
                        imageVector = Icons.Outlined.Cancel,
                        contentDescription = stringResource(R.string.deleted_favourite),
                        tint = Color(0xFFd68118),
                        modifier =
                        Modifier.testTag("${favourite.city}_Delete").clickable {
                            searchScreenViewModel.deleteFavourite(favourite)
                        })
                }
            }
        }
    }
}