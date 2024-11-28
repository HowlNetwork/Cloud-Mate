package com.example.cloudmate.network.weatherapi

import android.util.Log
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import retrofit2.Response

class WeatherApiRepositoryTest {
    private lateinit var weatherApi: WeatherApi
    private lateinit var repository: WeatherApiRepository

    private val exampleJson = """
        {
            "location": {
                "name": "London",
                "region": "City of London, Greater London",
                "country": "United Kingdom",
                "lat": 51.5171,
                "lon": -0.1062,
                "tz_id": "Europe/London",
                "localtime_epoch": 1731753102,
                "localtime": "2024-11-16 10:31"
            },
            "current": {
                "last_updated_epoch": 1731753000,
                "last_updated": "2024-11-16 10:30",
                "temp_c": 8.1,
                "temp_f": 46.6,
                "is_day": 1,
                "condition": {
                    "text": "Partly cloudy",
                    "icon": "//cdn.weatherapi.com/weather/64x64/day/116.png",
                    "code": 1003
                },
                "wind_mph": 5.4,
                "wind_kph": 8.6,
                "wind_degree": 251,
                "wind_dir": "WSW",
                "pressure_mb": 1019.0,
                "pressure_in": 30.09,
                "precip_mm": 0.0,
                "precip_in": 0.0,
                "humidity": 87,
                "cloud": 75,
                "feelslike_c": 6.6,
                "feelslike_f": 43.9,
                "windchill_c": 6.2,
                "windchill_f": 43.1,
                "heatindex_c": 7.7,
                "heatindex_f": 45.9,
                "dewpoint_c": 4.2,
                "dewpoint_f": 39.5,
                "vis_km": 10.0,
                "vis_miles": 6.0,
                "uv": 0.5,
                "gust_mph": 7.5,
                "gust_kph": 12.1,
                "air_quality": {
                    "co": 347.8,
                    "no2": 57.72,
                    "o3": 36.0,
                    "so2": 6.475,
                    "pm2_5": 20.905,
                    "pm10": 28.86,
                    "us-epa-index": 2,
                    "gb-defra-index": 2
                }
            }
        }
    """.trimIndent()

    @Before
    fun setup() {
        weatherApi = mock(WeatherApi::class.java)
        repository = WeatherApiRepository(weatherApi)
    }

    @Test
    fun `getCurrentWeather should return success when API call is successful`() = runBlocking {
        // Arrange
        val mockWeather = Gson().fromJson(exampleJson, Weather::class.java)
        val mockResponse = Response.success(mockWeather)
        `when`(weatherApi.getCurrentWeather("12.34,56.78", "no")).thenReturn(mockResponse)

        // Act
        val response = repository.getCurrentWeather(12.34f, 56.78f)

        // Assert
        response.success?.let { assertTrue(it) }
        assertNotNull(response.data)
        assertEquals(mockWeather, response.data)
    }

    @Test
    fun `getCurrentWeather should return failure when API call throws an exception`(): Unit = runBlocking {
        // Arrange
        `when`(weatherApi.getCurrentWeather("12.34,56.78", "no"))
            .thenThrow(RuntimeException("Network error"))

        // Act
        val response = repository.getCurrentWeather(12.34f, 56.78f)

        // Assert
        response.success?.let { assertFalse(it) }
        assertNull(response.data)
        assertNotNull(response.e)
        assertEquals("Network error", response.e?.message)
    }

    @Test
    fun `getForecastWeather should return failure when days are out of range`() = runBlocking {
        // Act
        val response = repository.getForecastWeather(12.34f, 56.78f, days = 11)

        // Assert
        response.success?.let { assertFalse(it) }
        assertNull(response.data)
        assertEquals("Days params smaller than 1 or bigger than 10", response.e?.message)
    }
}