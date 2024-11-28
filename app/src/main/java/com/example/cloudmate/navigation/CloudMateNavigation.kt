package com.example.cloudmate.navigation

import SearchScreen
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.cloudmate.screens.search.SearchScreenViewModel
import com.example.cloudmate.screens.setting.SettingScreen
import com.example.cloudmate.screens.splash.SplashScreen
import com.example.cloudmate.widgets.BottomNavItem

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CloudMateNavigation(
    context: Context
) {
    val navController = rememberNavController()
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val forecastViewModel = hiltViewModel<ForecastViewModel>()
    val searchScreenViewModel = hiltViewModel<SearchScreenViewModel>()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        val homeRoute = BottomNavItem.Home.route
        composable("$homeRoute/{city}", arguments = listOf(navArgument(name = "city") {
            type = NavType.StringType
        })) { navBack ->
            navBack.arguments?.getString("city").let { city ->
                HomeScreen(
                    navController = navController,
                    context = context,
                    city = city,
                    homeViewModel = homeViewModel,
                    forecastViewModel = forecastViewModel
                )
            }
        }

        composable(BottomNavItem.Forecast.route) {
            ForecastScreen(navController = navController,forecastViewModel, homeViewModel)
        }
        composable(BottomNavItem.Search.route) {
            SearchScreen(
                context,
                navController = navController,
                searchScreenViewModel,
                homeViewModel
            )
        }
        composable(BottomNavItem.Settings.route) {
            SettingScreen(navController = navController)
        }
    }

}