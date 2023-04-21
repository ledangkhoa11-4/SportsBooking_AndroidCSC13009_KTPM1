package com.example.sportbooking.Adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.sportbooking.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyBookingListViewAdapter(private val context: Activity, private val bookingHistories:ArrayList<com.example.sportbooking.DTO.BookingHistory>) : ArrayAdapter<com.example.sportbooking.DTO.BookingHistory>(context,
    R.layout.my_booking_item_layout,bookingHistories){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView: View = inflater.inflate(R.layout.my_booking_item_layout, null, true)
        val timeBookingTv = rowView.findViewById(R.id.datetimeDetailBooking) as TextView
        val courtName = rowView.findViewById(R.id.courtNameBooking) as TextView
        val courtImage = rowView.findViewById(R.id.courtImageBooking) as ImageView
        val typeBookingTv = rowView.findViewById(R.id.typeBooking) as TextView
        val typeSportImage = rowView.findViewById<ImageView>(R.id.typeSportImageBooking)
        val courtLocation = rowView.findViewById(R.id.locationBooking) as TextView
        val totalPriceBooking = rowView.findViewById(R.id.totalPriceBooking) as TextView

        val bookingItem = bookingHistories[position]
        timeBookingTv.text = "${convertTime(bookingItem.Time[0])} - ${convertTime(bookingItem.Time[1])}, ${convertDate(bookingItem.Date)}"
        courtName.text = bookingItem.Court!!.Name
        if(bookingItem.Court!!.bitmapArrayList.size > 0)
            courtImage.setImageBitmap(bookingItem.Court!!.bitmapArrayList[0])
        typeBookingTv.text = bookingItem.Court!!.Type
        val drawableID = context.resources.getIdentifier("${bookingItem.Court!!.Type.lowercase()}_icon","drawable",context.packageName)

        typeSportImage.setImageDrawable(context.resources.getDrawable(drawableID))
        courtLocation.text = bookingItem.Court!!.location?.addressName
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
