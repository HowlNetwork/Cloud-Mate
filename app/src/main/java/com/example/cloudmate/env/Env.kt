package com.example.cloudmate.env

import io.github.cdimascio.dotenv.dotenv

data class Env(
    val weatherApiEndpoint: String,
    val weatherApiKey: String
)

fun NewEnv(): Env{
    val dotenv = dotenv {
        directory = "/assets"
        filename = "env"
    }

    return Env(
        weatherApiEndpoint = dotenv["WEATHER_API_ENDPOINT"],
        weatherApiKey = dotenv["WEATHER_API_KEY"]
    )
}