package com.example.cloudmate.network.weatherapi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET(value = "/current.json")
    suspend fun getCurrentWeather(
        @Query("q") q: String,
        @Query("aqi") aqi: String
    ): Response<List<Weather>>
}