package com.example.sportbooking_owner.Adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.sportbooking_owner.DTO.BookingHistory
import com.example.sportbooking_owner.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class BookingAdapter(private val context: Activity, private val bookingHistories:ArrayList<BookingHistory>) : ArrayAdapter<BookingHistory>(context,
    R.layout.booking_item_layout,bookingHistories){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView: View = inflater.inflate(R.layout.booking_item_layout, null, true)
        val timeBookingTv = rowView.findViewById(R.id.datetimeDetailBooking) as TextView
        val cusName = rowView.findViewById(R.id.customerName) as TextView
        val customerImage = rowView.findViewById(R.id.courtImageBooking) as ImageView
        val yardNum = rowView.findViewById(R.id.yardNum) as TextView
        val totalPriceBooking = rowView.findViewById(R.id.totalPriceBooking) as TextView

        val bookingItem = bookingHistories[position]
        timeBookingTv.text = "${convertTime(bookingItem.Time[0])} - ${convertTime(bookingItem.Time[1])}, ${convertDate(bookingItem.Date)}"
        cusName.text = "Cus: ${bookingItem.CustomerName}"
        if(bookingItem.CustomerImage != null)
            customerImage.setImageBitmap(bookingItem.CustomerImage)
        yardNum.text = "Yard number: ${bookingItem.Yard}"
        totalPriceBooking.text = formatPrice(bookingItem.TotalPrice)
        return rowView
    }
    fun convertTime(timeStamp:Long):String{
        val sdf = SimpleDateFormat("HH:mm") // create a SimpleDateFormat object with desired format
        val formattedTime = sdf.format(Date(timeStamp)) // convert timestamp to date and format as string
        return formattedTime
    }
    fun convertDate(timeStamp:Long):String{
        val sdf = SimpleDateFormat("dd/MM/yyyy") // create a SimpleDateFormat object with desired format
        val formattedTime = sdf.format(Date(timeStamp)) // convert timestamp to date and format as string
        return formattedTime
    }
    fun formatPrice(price: Int): String {
        val formatter = java.text.DecimalFormat("#,###")
        return "Paid:" + formatter.format(price) + "đ"
    }
}