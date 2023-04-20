package com.example.sportbooking_owner.DTO

import android.os.Parcelable
import com.example.sportbooking_owner.DTO.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
class Location(var addressName: String = "",var latLng: LatLng = LatLng(
    0.0,
    0.0
)
): Parcelable {

}