package com.example.cloudmate.navigation

import SearchScreen
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.NavType
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.navArgument
import androidx.navigation.testing.TestNavHostController
import com.example.cloudmate.HiltTestActivity
import com.example.cloudmate.assertion.assertCurrentRouteName
import com.example.cloudmate.forecastscreen.FakeForecastViewModel
import com.example.cloudmate.homescreen.FakeHomeViewModel
import com.example.cloudmate.screens.forecast.ForecastScreen
import com.example.cloudmate.screens.home.HomeScreen
import com.example.cloudmate.screens.setting.SettingScreen
import com.example.cloudmate.searchscreen.FakeSearchScreenViewModel
import com.example.cloudmate.widgets.NavBar
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CloudMateNavigationWithNavBarTest {

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
            startDestination = Screen.Home.route + "/default"
        ) {
            composable(Screen.Home.route + "/{City}", arguments = listOf(navArgument("City") {
                type = NavType.StringType
            })) { navBackStackEntry ->
                val city = navBackStackEntry.arguments?.getString("City") ?: "default"
                HomeScreen(
                    navController = this@setGraphWithRoutes,
                    context = composeTestRule.activity,
                    city = city,
                    homeViewModel = homeViewModel,
                    forecastViewModel = forecastViewModel
                )
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    context = composeTestRule.activity,
                    navController = this@setGraphWithRoutes,
                    searchScreenViewModel = searchScreenViewModel,
                    homeViewModel = homeViewModel
                )
            }
            composable(Screen.Forecast.route) {
                ForecastScreen(
                    navController = this@setGraphWithRoutes,
                    forecastViewModel = forecastViewModel,
                    homeViewModel = homeViewModel
                )
            }
            composable(Screen.Settings.route) {
                SettingScreen(navController = this@setGraphWithRoutes)
            }
        }
        this.graph = navGraph
    }

    @Before
    fun setupNavController() {
        hiltRule.inject()

        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
                setGraphWithRoutes()
            }
            CompositionLocalProvider {
                NavBar(navController = navController)
            }
        }
    }

    @Test
    fun test_navigation_byClickingNavBarItems() = runBlocking {
        composeTestRule.runOnUiThread {
            // Start from Home Screen
            navController.navigate(Screen.Home.route + "/default")
        }
        composeTestRule.waitForIdle()
        delay(3000L)

        // Navigate to Search Screen via NavBar
        composeTestRule.onNodeWithContentDescription("Searching Icon").performClick()
        composeTestRule.waitForIdle()
        navController.assertCurrentRouteName(Screen.Search.route)
        delay(3000L)

        // Navigate to Forecast Screen via NavBar
        composeTestRule.onNodeWithContentDescription("Forecast Icon").performClick()
        composeTestRule.waitForIdle()
        navController.assertCurrentRouteName(Screen.Forecast.route)
        delay(3000L)

        // Navigate back to Home Screen via NavBar
        composeTestRule.onNodeWithContentDescription("Home Icon").performClick()
        composeTestRule.waitForIdle()
        navController.assertCurrentRouteName(Screen.Home.route)
        delay(3000L)
    }
}
