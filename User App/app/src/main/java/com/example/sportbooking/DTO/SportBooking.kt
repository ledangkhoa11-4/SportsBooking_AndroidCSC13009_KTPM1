package com.example.sportbooking.DTO

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class SportBooking(var ID:String = "",
                   var CourtID:String = "",
                   var UserID:String = "",
                   var Date:Long = 0,
                   var Yard:Int = 0,
                   var Time:ArrayList<Long> = ArrayList(),
                   var TotalPrice:Int = 0) :Parcelable{
}