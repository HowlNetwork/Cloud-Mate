package com.example.cloudmate.network

import com.example.cloudmate.core.Env
import com.example.cloudmate.network.weatherapi.WeatherApi
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

object ApiModule {
    @Provides
    @Singleton
    fun provideWeatherApi(
        gsonConverterFactory: GsonConverterFactory,
        env: Env
    ): WeatherApi {
        val apiKeyInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()

            val newUrl: HttpUrl = originalRequest.url.newBuilder()
                .addQueryParameter("key", env.weatherApiKey)
                .build()

            val newRequest: Request = originalRequest.newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(env.weatherApiEndpoint)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

        return retrofit.create(WeatherApi::class.java)
    }
}