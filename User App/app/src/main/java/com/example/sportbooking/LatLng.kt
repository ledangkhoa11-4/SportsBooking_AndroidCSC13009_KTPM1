package com.example.sportbooking

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class LatLng :Parcelable{
    public var latitude: Double = 0.0
    public var longitude: Double = 0.0
    fun LatLng() {}
}