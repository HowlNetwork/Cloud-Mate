package com.example.cloudmate.network.floodapi

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface FloodCheckApi {
    @GET("check_flood")
    suspend fun getFloodCheck(
        @Query("location") location: String,
        @Query("timestamp") timestamp: Long
    ): Response<FloodCheck>
    companion object {
        fun create(baseUrl: String): FloodCheckApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(FloodCheckApi::class.java)
        }
    }
}
