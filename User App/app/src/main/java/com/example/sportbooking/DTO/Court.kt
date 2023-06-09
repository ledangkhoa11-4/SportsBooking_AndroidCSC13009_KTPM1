package com.example.sportbooking


import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import com.example.sportbooking.DTO.Location
import kotlinx.parcelize.Parcelize

@Parcelize
public class Court(
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
    var avgRating: Double = 0.0,
    var numRating: Int = 0,
    var numBooking: Int = 0,
    var courtDistance: Double = 0.0,
    var numOfYards:Int = 1,
    var isFavorite:Boolean = false
):Parcelable {

}
class DistanceComparator : Comparator<Court?> {
    override fun compare(p0: Court?, p1: Court?): Int {
        return p0!!.courtDistance!!.compareTo(p1!!.courtDistance)
    }
}
class PriceComparator : Comparator<Court?> {
    override fun compare(p0: Court?, p1: Court?): Int {

        return p0!!.Price!!.compareTo(p1!!.Price)
    }
}
class NumBookComparator : Comparator<Court?> {
    override fun compare(p0: Court?, p1: Court?): Int {

        return p1!!.numBooking!!.compareTo(p0!!.numBooking)
    }
}
class RatingComparator : Comparator<Court?> {
    override fun compare(p0: Court?, p1: Court?): Int {

        return p1!!.avgRating!!.compareTo(p0!!.avgRating)
    }
}