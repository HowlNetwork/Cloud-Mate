package com.example.cloudmate.navigation

import SearchScreen
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cloudmate.screens.forecast.ForecastScreen
import com.example.cloudmate.screens.forecast.ForecastViewModel
import com.example.cloudmate.screens.home.HomeScreen
import com.example.cloudmate.screens.home.HomeViewModel
import com.example.cloudmate.screens.setting.SettingScreen
import com.example.cloudmate.widgets.BottomNavItem

@Composable
fun WeatherNavigation(
    navController: NavHostController, context: Context
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val forecastViewModel = hiltViewModel<ForecastViewModel>()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {

        }
        val route = BottomNavItem.Home.route
        composable("$route/{city}", arguments = listOf(navArgument(name = "city") {
            type = NavType.StringType
        })) { navBack ->
            navBack.arguments?.getString("city").let { city ->
                HomeScreen(
                    navController =  navController,
                    context = context,
                    city = city,
                    homeViewModel = homeViewModel,
                    forecastViewModel = forecastViewModel
                )
            }
        }
        composable(BottomNavItem.Forecast.route) {
            ForecastScreen(navController = navController)
        }
        composable(BottomNavItem.Search.route) {
            SearchScreen(context, navController = navController)
        }
        composable(BottomNavItem.Settings.route) {
            SettingScreen(navController = navController)
        }
    }

}