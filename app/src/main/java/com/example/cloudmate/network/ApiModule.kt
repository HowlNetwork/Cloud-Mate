package com.example.cloudmate.network

import com.example.cloudmate.core.Env
import com.example.cloudmate.network.floodapi.FloodCheckApi
import com.example.cloudmate.network.weatherapi.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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
    @Provides
    @Singleton
    fun provideFloodCheckApi(
        gsonConverterFactory: GsonConverterFactory,
        env: Env // Biến môi trường chứa base URL
    ): FloodCheckApi {
        val okHttpClient = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl(env.floodCheckApiEndpoint) // Base URL từ biến môi trường
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

        return retrofit.create(FloodCheckApi::class.java)
    }


}