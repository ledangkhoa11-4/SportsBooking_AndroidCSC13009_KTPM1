package com.example.sportbooking.DTO

import android.os.Parcelable
import com.example.sportbooking.Court
import kotlinx.parcelize.Parcelize

@Parcelize
class BookingHistory(var ID:String = "",
                     var CourtID:String = "",
                     var UserID:String = "",
                     var Date:Long = 0,
                     var Yard:Int = 0,
                     var Time:ArrayList<Long> = ArrayList(),
                     var TotalPrice:Int = 0,
                     var Court: Court? = null,
                     var SecretID: Long = 0L,
                     var Status: Boolean = false ) :Parcelable{
}