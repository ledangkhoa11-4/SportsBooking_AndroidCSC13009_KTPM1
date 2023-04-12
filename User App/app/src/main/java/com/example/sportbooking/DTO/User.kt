package com.example.sportbooking.DTO

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

class User(
    var UserID:String = "",
    var Image:Bitmap? = null,
    var Name:String = "",
    var Email:String = "",
    var Gender:String = "",
    var Dob:Long = 0L,
    var Phone:String= "",
) {
}