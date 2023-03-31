package com.example.sportbooking_owner

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
class Location(var addressName: String,var latLng: LatLng):Parcelable {
}