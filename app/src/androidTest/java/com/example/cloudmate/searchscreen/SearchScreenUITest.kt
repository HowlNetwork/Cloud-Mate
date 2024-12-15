package com.example.cloudmate.searchscreen

import SearchScreen
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
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
import com.example.cloudmate.module.Favourite
import com.example.cloudmate.navigation.Screen
import com.example.cloudmate.screens.forecast.ForecastScreen
import com.example.cloudmate.screens.home.HomeScreen
import com.example.cloudmate.screens.setting.SettingScreen
import com.example.cloudmate.screens.splash.SplashScreen
import com.example.cloudmate.widgets.BottomNavItem
import com.example.cloudmate.widgets.NavBar
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SearchScreenUITest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private lateinit var navController: TestNavHostController
    private val fakeSearchScreenViewModel = FakeSearchScreenViewModel()
    private val fakeHomeViewModel = FakeHomeViewModel()
    private val fakeForecastViewModel = FakeForecastViewModel()

    private fun TestNavHostController.setGraphWithRoutes() {
        val navGraph = createGraph(
            startDestination = Screen.Search.route
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(navController = this@setGraphWithRoutes)
            }
            composable(
                route = "${BottomNavItem.Home.route}/{City}",
                arguments = listOf(navArgument("City") { type = NavType.StringType })
            ) { backStackEntry ->
                val city = backStackEntry.arguments?.getString("City") ?: ""
                HomeScreen(
                    navController = this@setGraphWithRoutes,
                    context = composeTestRule.activity,
                    city = city,
                    homeViewModel = fakeHomeViewModel,
                    forecastViewModel = fakeForecastViewModel
                )
            }
            composable(BottomNavItem.Forecast.route) {
                ForecastScreen(
                    navController = this@setGraphWithRoutes,
                    forecastViewModel = fakeForecastViewModel,
                    homeViewModel = fakeHomeViewModel
                )
            }
            composable(BottomNavItem.Search.route) {
                SearchScreen(
                    context = composeTestRule.activity,
                    navController = this@setGraphWithRoutes,
                    searchScreenViewModel = fakeSearchScreenViewModel,
                    homeViewModel = fakeHomeViewModel
                )
            }
            composable(BottomNavItem.Settings.route) {
                SettingScreen(navController = this@setGraphWithRoutes)
            }
        }
        this.graph = navGraph
    }


    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.setContent {
            navController = TestNavHostController(composeTestRule.activity).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
                setGraphWithRoutes()
            }
            SearchScreen(
                context = composeTestRule.activity,
                navController = navController,
                searchScreenViewModel = fakeSearchScreenViewModel,
                homeViewModel = fakeHomeViewModel
            )
        }
    }

    @Test
    fun testInitialUI() {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.pick_location))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.find_city))
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("Search Bar").assertIsDisplayed()
    }

    @Test
    fun testSearchCityAndNavigateToHome() {
        val cityName = "Hanoi"

        // Nhập thành phố và thực hiện điều hướng
        composeTestRule.onNodeWithTag("Search Bar").performTextInput(cityName)
        composeTestRule.onNodeWithTag("Search Bar").performImeAction()

        // Đợi cho đến khi Compose ổn định
        composeTestRule.waitForIdle()
        navController.assertCurrentRouteName(Screen.Home.route)
    }


    @Test
    fun testReturnToSearchScreenAndAddFavourite() {
        val cityName = "HoChiMinh"

        composeTestRule.onNodeWithTag("Search Bar").performTextInput(cityName)
        composeTestRule.onNodeWithTag("Search Bar").performImeAction()

        composeTestRule.runOnUiThread {
            navController.popBackStack()
        }
        // Assert all nodes with the tag "$cityName Card" are displayed
        composeTestRule.onNodeWithTag("$cityName Card").assertExists()

        assertTrue(fakeSearchScreenViewModel.favList.value.any { it.city == cityName })
    }

    @Test
    fun testDeleteFavourite() {
        val cityName = "New Jersey"

        // Navigate to the Search screen and perform a search
        composeTestRule.runOnUiThread {
            navController.navigate(Screen.Search.route)
        }

        composeTestRule.onNodeWithTag("Search Bar").performTextInput(cityName)
        composeTestRule.onNodeWithTag("Search Bar").performImeAction()

        // Wait for any UI changes to complete
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("${cityName} Card").assertExists()

        // Navigate back to Search screen
        composeTestRule.runOnUiThread {
            navController.popBackStack(Screen.Search.route, inclusive = false)
        }

        // Wait until the current route is Search
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            navController.currentDestination?.route == Screen.Search.route
        }
        navController.assertCurrentRouteName(Screen.Search.route)
        composeTestRule.onNodeWithTag("${cityName} Card").assertExists()
        // Assert that the content is correct
//        composeTestRule.onNodeWithTag("${cityName} Card").assertIsDisplayed()
    }


}
