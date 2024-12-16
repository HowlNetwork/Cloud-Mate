package com.example.cloudmate.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.cloudmate.worker.FloodAlertWorker
import java.util.concurrent.TimeUnit

class WorkManagerRepository(private val context: Context) {

    private val workManager = WorkManager.getInstance(context)

    fun scheduleWeatherAlert() {
        val workRequest = PeriodicWorkRequest.Builder(
            FloodAlertWorker::class.java,
            20, TimeUnit.MINUTES
        ).build()

        workManager.enqueueUniquePeriodicWork(
            WEATHER_ALERT_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancelWeatherAlert() {
        workManager.cancelUniqueWork(WEATHER_ALERT_WORK_NAME)
    }

    companion object {
        const val WEATHER_ALERT_WORK_NAME = "WeatherAlertWork"
    }
}