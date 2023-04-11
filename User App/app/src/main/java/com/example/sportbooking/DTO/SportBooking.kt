package com.example.sportbooking.DTO

class SportBooking(var ID:String = "",
                   var CourtID:String = "",
                   var UserID:String = "",
                   var Date:Long = 0,
                   var Time:ArrayList<Long> = ArrayList(),
                   var TotalPrice:Int = 0) {
}