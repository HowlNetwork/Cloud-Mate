package com.example.cloudmate.network.weatherapi

import com.example.cloudmate.core.CoreModule
import com.example.cloudmate.core.Env
import com.example.cloudmate.network.ApiModule
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class WeatherApiRepositoryTest {
    private lateinit var weatherApi: WeatherApi
    private lateinit var repository: WeatherApiRepository

    @Before
    fun setup() {
        val mockEnv = Env(
            weatherApiEndpoint = "https://api.weatherapi.com/",
            weatherApiKey = "7b08e5fa126f417c9cb203330241811",
            floodCheckApiEndpoint = "https://api.weatherapi.com/"
        )

        weatherApi = ApiModule.provideWeatherApi(
            CoreModule.provideGson(),
            mockEnv
        )
        repository = WeatherApiRepository(weatherApi)
    }

    @Test
    fun `getCurrentWeather should return success when API call is successful`(): Unit =
        runBlocking {
            val response = repository.getCurrentWeather("London")

            // Assert
            assertTrue(response.success == true)
            assertNotNull(response.data)
        }


    @Test
    fun `getCurrentWeather should return failure when API not found location`(): Unit =
        runBlocking {
            val response = repository.getCurrentWeather(12.34f, 56.78f)

            // Assert
            assertTrue(response.success == false)
            assertNull(response.data)
        }

    @Test
    fun `getForecastWeather should return failure when days are out of range`() = runBlocking {
        // Act
        val response = repository.getForecastWeather(12.245f, 45.254f, days = 11)

        // Assert
        assertTrue(response.success == false)
        assertNull(response.data)
        assertEquals("Days params smaller than 1 or bigger than 10", response.e?.message)
    }
}
