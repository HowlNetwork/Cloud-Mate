package com.example.cloudmate.navigation

import SearchScreen
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavType
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.navArgument
import androidx.navigation.testing.TestNavHostController
import com.example.cloudmate.HiltTestActivity
import com.example.cloudmate.R
import com.example.cloudmate.assertion.assertCurrentRouteName
import com.example.cloudmate.forecastscreen.FakeForecastViewModel
import com.example.cloudmate.homescreen.FakeHomeViewModel
import com.example.cloudmate.screens.forecast.ForecastScreen
import com.example.cloudmate.screens.home.HomeScreen
import com.example.cloudmate.screens.setting.SettingScreen
import com.example.cloudmate.screens.splash.SplashScreen
import com.example.cloudmate.searchscreen.FakeSearchScreenViewModel
import com.example.cloudmate.widgets.BottomNavItem
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CloudMateNavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private lateinit var navController: TestNavHostController
    private val homeViewModel = FakeHomeViewModel()
    private val forecastViewModel = FakeForecastViewModel()
    private val searchScreenViewModel = FakeSearchScreenViewModel()
    private fun TestNavHostController.setGraphWithRoutes() {
        val navGraph = createGraph(
            startDestination = Screen.Splash.route
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(navController = navController)
            }
            val homeRoute = BottomNavItem.Home.route
            composable("$homeRoute/{City}", arguments = listOf(navArgument(name = "City") {
                type = NavType.StringType
            })) { navBack ->
                navBack.arguments?.getString("City").let { city ->
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
        this.graph = navGraph
    }


    @Before
    fun setupNavController() {
        hiltRule.inject() // Inject Hilt dependencies

        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
                setGraphWithRoutes()
            }
            CloudMateNavigation(context = composeTestRule.activity)
        }
    }

    @Test
    fun startDestination_isSplashScreen() {
        // Kiểm tra màn hình ban đầu là SplashScreen
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Splash Weather").assertIsDisplayed()
    }

    @Test
    fun navigate_toHomeScreen() = runBlocking {
        composeTestRule.runOnUiThread {
            navController.navigate("${Screen.Home.route}/City")
        }
        composeTestRule.waitForIdle()

        // Introduce a delay to allow the screen to be visible
        delay(1000L) // 1 second delay

        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navigate_toSearchScreen() = runBlocking {
        composeTestRule.runOnUiThread {
            navController.navigate(Screen.Search.route)
        }
        composeTestRule.waitForIdle()

        // Introduce a delay to allow the screen to be visible
        delay(1000L) // 1 second delay

        navController.assertCurrentRouteName(Screen.Search.route)
    }

    @Test
    fun navigate_toForecastScreen() = runBlocking {
        composeTestRule.runOnUiThread {
            navController.navigate(Screen.Forecast.route)
        }
        composeTestRule.waitForIdle()

        // Introduce a delay to allow the screen to be visible
        delay(1000L) // 1 second delay

        navController.assertCurrentRouteName(Screen.Forecast.route)
    }

    @Test
    fun navigate_toSettingsScreen() = runBlocking {
        composeTestRule.runOnUiThread {
            navController.navigate(Screen.Settings.route)
        }
        composeTestRule.waitForIdle()

        // Introduce a delay to allow the screen to be visible
        delay(1000L) // 1 second delay

        navController.assertCurrentRouteName(Screen.Settings.route)
    }
}