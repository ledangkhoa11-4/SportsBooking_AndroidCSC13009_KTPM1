package com.example.sportbooking.DTO

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class LatLng(var latitude: Double  = 0.0, var longitude: Double = 0.0) :Parcelable{
    fun LatLng() {}

}