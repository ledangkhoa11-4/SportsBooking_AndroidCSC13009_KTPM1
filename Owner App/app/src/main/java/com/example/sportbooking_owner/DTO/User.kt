package com.example.sportbooking_owner.DTO

import android.graphics.Bitmap

class User(
    var id:String = "",
    var username:String = "",
    var email:String = "",
    var Image: Bitmap? = null,
    var Gender:String = "",
    var Dob:Long = 0L,
    var Phone:String= "") {

}