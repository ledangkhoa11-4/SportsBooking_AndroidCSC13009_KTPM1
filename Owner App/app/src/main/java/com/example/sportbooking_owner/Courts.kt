package com.example.sportbooking_owner

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

@Parcelize
public class Courts(
    var CourtID:Long = 0L,
    var OwnerID:String = "",
    var Name:String = "",
    var Type:String = "",
    var Phone:String = "",
    var numOfYards:Int = 0,
    var location: Location? = null,
    var ServiceWeekdays:String = "",
    var ServiceHour:ArrayList<Long> = ArrayList(),
    var Images:ArrayList<String> = ArrayList(),
    var Description: String = "",
    var AvalableService:ArrayList<String> = ArrayList(),
    var Price:Int = 0
):Parcelable {

}