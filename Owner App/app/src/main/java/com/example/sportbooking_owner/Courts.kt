package com.example.sportbooking_owner

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

@Parcelize
public class Courts(
    var CourtID:String = "",
    var OwnerID:String = "",
    var Name:String = "",
    var Type:String = "",
    var location: Location? = null,
    var Phone:String = "",
    var ServiceWeekdays:String = "",
    var ServiceHour:ArrayList<Long> = ArrayList(),
    var Images:ArrayList<String> = ArrayList(),
    var bitmapArrayList:ArrayList<Bitmap> = ArrayList(),
    var Description: String = "",
    var AvalableService:ArrayList<String> = ArrayList(),
    var Price:Int = 0,
    var numOfYards:Int = 1
):Parcelable {

}