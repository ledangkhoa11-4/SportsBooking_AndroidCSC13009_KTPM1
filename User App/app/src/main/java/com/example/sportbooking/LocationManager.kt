package com.example.sportbooking

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException

import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class LocationManager(var activity: Activity) {
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(activity)
    }
    private val locationRequest: LocationRequest by lazy {
        LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 7000 // the fastest interval in which you want to receive updates
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // set the priority to high accuracy
        }
    }
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.lastLocation?.let {
                val loc = Location()

                loc.latLng.latitude = it.latitude
                loc.latLng.longitude = it.longitude
                Log.i("Update location",loc.latLng.latitude.toString() + "-" + loc.latLng.longitude.toString())
                MainActivity.saveLocation(loc,activity);
                MainActivity.lastLocation = loc;
                HomeActivity.updateDistance()
            }
        }
    }

    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            turnOnGPS(activity)
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

        } else {
            requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                2000
            )
        }
    }
    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    private fun turnOnGPS(context: Context) {
        try {
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            builder.setAlwaysShow(true)

            val result: Task<LocationSettingsResponse> = LocationServices.getSettingsClient(
                context.applicationContext
            )
                .checkLocationSettings(builder.build())
            result.addOnCompleteListener { task ->
                try {
                    val response = task.getResult(ApiException::class.java)

                } catch (e: ApiException) {
                    when (e.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val resolvableApiException = e as ResolvableApiException
                            resolvableApiException.startResolutionForResult(activity, 2)
                        } catch (ex: IntentSender.SendIntentException) {
                            ex.printStackTrace()
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                    }
                }
            }

        } catch (e: Exception) {
            // If reflection fails, open settings page
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    fun handleOnRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 2000){
            if ((grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED)) {

                this.startLocationUpdates()
            } else {
                this.stopLocationUpdates()
                Toast.makeText(activity, "Location access permission not granted",Toast.LENGTH_SHORT).show()
            }
        }
    }

}