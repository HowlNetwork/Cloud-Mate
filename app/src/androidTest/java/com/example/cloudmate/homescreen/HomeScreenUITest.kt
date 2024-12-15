package com.example.cloudmate.homescreen

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.cloudmate.HiltTestActivity
import com.example.cloudmate.MainActivity
import com.example.cloudmate.contracts.IForecastScreenViewModel
import com.example.cloudmate.contracts.IHomeScreenViewModel
import com.example.cloudmate.forecastscreen.FakeForecastViewModel
import com.example.cloudmate.screens.home.HomeScreen
import com.example.cloudmate.ui.theme.CloudMateTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@LargeTest
@HiltAndroidTest
class HomeScreenUiTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private lateinit var homeViewModel: IHomeScreenViewModel
    private lateinit var forecastViewModel: IForecastScreenViewModel
    private lateinit var context: Context

    @Before
    fun setup() {
        homeViewModel = FakeHomeViewModel()
        forecastViewModel = FakeForecastViewModel()
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testHomeScreenDisplaysCorrectly() {
        composeTestRule.setContent {
            CloudMateTheme {
                HomeScreen(
                    navController = androidx.navigation.compose.rememberNavController(),
                    city = "default",
                    context = context,
                    homeViewModel = homeViewModel,
                    forecastViewModel = forecastViewModel
                )
            }
        }
        composeTestRule.waitForIdle()
        // Verify that the HomeScreen is displayed
        composeTestRule.onNodeWithContentDescription("Home Icon").assertIsDisplayed()

        // Verify that the location text is displayed
        composeTestRule.onNodeWithTag("CityName").assertIsDisplayed()
    }

    @Test
    fun testUnknownLocationDisplaysToast() {
        composeTestRule.setContent {
            CloudMateTheme {
                HomeScreen(
                    navController = androidx.navigation.compose.rememberNavController(),
                    city = "unknownCity",
                    context = context,
                    homeViewModel = homeViewModel,
                    forecastViewModel = forecastViewModel
                )
            }
        }

        // Simulate invalid location and ensure fallback behavior
        composeTestRule.onNodeWithTag("CityName").assertIsDisplayed()
        composeTestRule.onNodeWithText("Unknown Location").assertIsDisplayed()
    }

    @Test
    fun testWeatherInformationDisplaysCorrectly() {
        composeTestRule.setContent {
            CloudMateTheme {
                HomeScreen(
                    navController = androidx.navigation.compose.rememberNavController(),
                    city = "Hanoi",
                    context = context,
                    homeViewModel = homeViewModel,
                    forecastViewModel = forecastViewModel
                )
            }
        }

        // Check if the weather information (e.g., temperature, pressure, wind) is displayed
        composeTestRule.onNodeWithText("Pressure").assertIsDisplayed()
        composeTestRule.onNodeWithText("Wind").assertIsDisplayed()
        composeTestRule.onNodeWithText("Humidity").assertIsDisplayed()
    }
}
