package com.example.sportbooking

import com.example.sportbooking.DTO.Location

class GetDistance() {
    companion object{
        fun getDistance(original: Location, destination: Location):Float{
            val originLocation = android.location.Location("")
            originLocation.latitude = original.latLng.latitude
            originLocation.longitude = original.latLng.longitude

            val destinationLocation = android.location.Location("")
            destinationLocation.latitude = destination.latLng.latitude
            destinationLocation.longitude = destination.latLng.longitude

            val distanceInMeters = originLocation.distanceTo(destinationLocation)
           return distanceInMeters
        }

        fun formatNumber(distance: Double): String {
            return when {
                distance < 1000 -> {
                    String.format("%.0f m", distance)
                }
                distance < 1_000_000 -> {
                    String.format("%.2f km", distance / 1000)
                }
                distance < 1_000_000_000 -> {
                    String.format("%.2fM km", distance / 1_000_000)
                }
                else -> {
                    String.format("%.2fB km", distance / 1_000_000_000)
                }
            }
        }
    }
}