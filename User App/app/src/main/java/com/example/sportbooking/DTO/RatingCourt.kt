package com.example.sportbooking.DTO

class RatingCourt (
    var ID:String? = "",
    var bookingHistoryID:String? = "",
    var courtID:String? = "",
    var star: Float? = 0.0f,
    var content:String? = "",
    var improvement:ArrayList<Boolean>? = ArrayList()) {
}