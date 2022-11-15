package com.seamlabs.mvpdriver.common.utility

import android.app.Activity
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.location.FusedLocationProviderClient


class LocationHelper(
    private val context: Activity,
    private val location: (Location?) -> Unit = {},
) {

    companion object {
        private const val TAG = "LocationHelper"
    }

    private var fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private lateinit var locationRequest: LocationRequest
    private var locationCallback: LocationCallback? = null

    init {
        buildLocationRequest()
        buildLocationCallBack()
        subscribeToLocationUpdates()
    }

    private fun subscribeToLocationUpdates() {
        Log.d(Companion.TAG, "subscribeToLocationUpdates()")
        try {
            locationCallback?.let {
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest, it, Looper.getMainLooper()
                )
            }

        } catch (unlikely: SecurityException) {
            Log.e(Companion.TAG, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }


    private fun unsubscribeToLocationUpdates() {
        Log.d(Companion.TAG, "unsubscribeToLocationUpdates()")

        try {
            locationCallback?.let {
                val removeTask = fusedLocationProviderClient.removeLocationUpdates(it)
                removeTask.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(Companion.TAG, "Location Callback removed.")
                    } else {
                        Log.d(Companion.TAG, "Failed to remove Location Callback.")
                    }
                }
            }

        } catch (unlikely: SecurityException) {
            Log.e(Companion.TAG, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }


    private fun buildLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 3000
            fastestInterval = 3000
            smallestDisplacement = 3f
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun buildLocationCallBack() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                location.invoke(locationResult.lastLocation)
                stopLocation()
            }
        }
    }

    fun stopLocation() {
        unsubscribeToLocationUpdates()
        locationCallback?.let {
            fusedLocationProviderClient.removeLocationUpdates(it)
        }
    }

}