package com.example.cloudmate.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cloudmate.core.CoreModule
import com.example.cloudmate.network.ApiModule
import com.example.cloudmate.network.common.AppResponse
import com.example.cloudmate.network.floodapi.FloodCheck
import com.example.cloudmate.network.floodapi.FloodCheckApiRepository
import javax.inject.Inject

@HiltWorker
class FloodAlertWorker @Inject constructor(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    private val floodCheckApiRepository: FloodCheckApiRepository = FloodCheckApiRepository(
        ApiModule.provideFloodCheckApi(
            CoreModule.provideGson(),
            CoreModule.provideEnv()
        )
    )

    override suspend fun doWork(): Result {
        try {
            Log.d("FloodAlertWorker", "doWork started")

            if (!isNotificationPermissionGranted(applicationContext)) {
                Log.e("FloodAlertWorker", "Notification permission not granted")
                return Result.failure()
            }
            val location = "Cầu Giấy"
            val timestamp = System.currentTimeMillis()
            // Gọi API trực tiếp thay vì sử dụng `produceState`
            val floodResponse = checkFloodAlert(location, timestamp)

            if (floodResponse.success == true && floodResponse.data != null) {
                val floodAlert = floodResponse.data
                if (floodAlert != null) {
                    if(floodAlert.ratios.floodRatio >= 0.5) {
                    makeFloodNotification(
                        message = "Lũ lụt được phát hiện tại ${floodAlert.location}. Hãy cẩn thận!",
                        context = applicationContext
                    )
                    }
                    else {
                        makeFloodNotification(
                            message = "Lũ lụt không được phát hiện tại ${floodAlert.location}.",
                            context = applicationContext
                        )
                    }
                }
            } else {
                Log.e("FloodAlertWorker", "API call succeeded but data is null or invalid.")
            }


            return Result.success()
        } catch (e: Exception) {
            Log.e("FloodAlertWorker", "Error in doWork: ${e.message}")
            return Result.failure()
        }
    }

    private fun isNotificationPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private suspend fun checkFloodAlert(location : String, timestamp : Long): AppResponse<FloodCheck, Boolean, Exception> {
        // Giữ nguyên cách gọi instance của `FloodCheckApiRepository`
        return floodCheckApiRepository.getFloodCheck(location, timestamp)
    }
}

