package com.example.cloudmate.network.weatherapi

import android.util.Log
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
//
//class WeatherApiRepositoryTest {
//    private lateinit var weatherApi: WeatherApi
//    private lateinit var weatherApiRepository: WeatherApiRepository
//
//    @Before
//    fun setup() {
//        weatherApi = mock()
//        weatherApiRepository = WeatherApiRepository(weatherApi)
//    }
//
//    @Test
//    fun getCurrentWeather_testGetSuccess() = runTest {
//        val response = weatherApiRepository.getCurrentWeather(128.0f, 128.0f)
//        if (response.success == true){
//            Log.d("TestGetWeather", "data: ${response.data}")
//        }else{
//            Log.d("TestGetWeather", "Fail")
//        }
//        assert(true)
//    }
//}