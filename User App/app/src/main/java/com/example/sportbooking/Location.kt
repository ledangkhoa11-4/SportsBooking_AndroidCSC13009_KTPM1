package com.example.sportbooking

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
class Location(var addressName: String = "",var latLng: LatLng = LatLng(
    0.0,
    0.0
)
): Parcelable {
}