package com.example.sportbooking


import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
public class Court(
    var CourtID:Long = 0,
    var OwnerID:Long = 0,
    var Name:String = "",
    var Type:String = "",
    var location:Location? = null,
    var Phone:String = "",
    var ServiceWeekdays:String = "",
    var ServiceHour:ArrayList<Long> = ArrayList(),
    var Images:ArrayList<String> = ArrayList(),
    var bitmapArrayList:ArrayList<Bitmap> = ArrayList(),
    var ImagesTemp:ArrayList<Uri> = ArrayList(),
    var Description: String = "",
    var AvalableService:ArrayList<String> = ArrayList(),
    var Price:Int = 0,
    var avgRating: Double = 0.0,
    var numRating: Int = 0,
    var numBooking: Int = 0,
    var courtDistance: Double = 0.0,
):Parcelable {

}