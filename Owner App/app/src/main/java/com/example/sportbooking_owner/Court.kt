package com.example.sportbooking_owner

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

@Parcelize
public class Court(
    var CourtID:Int = 0,
    var OwnerID:Int = 0,
    var Name:String = "",
    var Type:String = "",
    var ServiceWeekdays:String = "",
    var ServiceHour:ArrayList<Timestamp> = ArrayList(),
    var Images:ArrayList<Bitmap> = ArrayList(),
    var ImagesTemp:ArrayList<Uri> = ArrayList(),
    var Description: String = "",
    var AvalableService:ArrayList<String> = ArrayList(),
    var Price:Int = 0
):Parcelable {

}