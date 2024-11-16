package com.example.cloudmate.core

import io.github.cdimascio.dotenv.dotenv

class Env(
    val weatherApiEndpoint: String,
    val weatherApiKey: String,
){
    companion object{
        fun newEnv(): Env {
            val dotenv = dotenv {
                directory = "/assets"
                filename = "env"
            }

            return Env(
                weatherApiEndpoint = dotenv["WEATHER_API_ENDPOINT"],
                weatherApiKey = dotenv["WEATHER_API_KEY"]
            )
        }
    }
}



