package com.example.cloudmate.network.floodapi

import android.annotation.SuppressLint
import com.example.cloudmate.network.common.AppResponse
import javax.inject.Inject

class FloodCheckApiRepository @Inject constructor(private val api: FloodCheckApi) {

    @SuppressLint("DefaultLocale")
    suspend fun getFloodCheck(
        location: String,
        timestamp: Long
    ): AppResponse<FloodCheck, Boolean, Exception> {
        val response = try {
            api.getFloodCheck(location, timestamp) // Gọi API qua Retrofit
        } catch (e: Exception) {
            return AppResponse(data = null, success = false, e = e)
        }

        if (!response.isSuccessful) {
            return AppResponse(data = null, success = false, e = Exception("HTTP ${response.code()}"))
        }

        val floodCheck: FloodCheck = response.body()
            ?: return AppResponse(data = null, success = false, e = Exception("Empty response body"))


        return AppResponse(data = floodCheck, success = true, e = null)
    }
}
