package com.example.sportbooking.DTO

import android.graphics.Bitmap
import java.util.*
import kotlin.collections.ArrayList

class RatingCourt (
    var id:String? ="",
    var bookingHistoryID:String? = "",
    var courtID:String? = "",
    var userName:String? = "",
    var userImage:Bitmap? = null,
    var star: Float? = 0.0f,
    var content:String? = "",
    var improvement:ArrayList<Boolean>? = ArrayList(),
    var dateRate: Long = System.currentTimeMillis())
{
}