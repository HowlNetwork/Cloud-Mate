package com.example.cloudmate.location

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LastLocationRequest
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks

interface IFusedLocationProviderClient {
    fun getLastLocation(): Task<Location>
    fun getLastLocation(request: LastLocationRequest): Task<Location>
}


class FakeFusedLocationProviderClient : IFusedLocationProviderClient {
    override fun getLastLocation(): Task<Location> {
        val fakeLocation = Location("fake_provider").apply {
            latitude = 10.762622
            longitude = 106.660172
        }
        return Tasks.forResult(fakeLocation)
    }

    override fun getLastLocation(request: LastLocationRequest): Task<Location> {
        val fakeLocation = Location("fake_provider").apply {
            latitude = 10.762622
            longitude = 106.660172
        }
        return Tasks.forResult(fakeLocation)
    }
}
