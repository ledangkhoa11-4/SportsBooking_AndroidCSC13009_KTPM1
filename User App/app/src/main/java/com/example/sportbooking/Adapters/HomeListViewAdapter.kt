package com.example.sportbooking.Adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.sportbooking.Court
import com.example.sportbooking.R
import com.example.sportbooking.Ultils.GetDistance

class   homeListViewAdapter(private val context:Activity, private val courtInfo:ArrayList<Court>):
    ArrayAdapter<Court>(context, R.layout.list_court_item,courtInfo) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView: View = inflater.inflate(R.layout.list_court_item, null, true)
        val courtName = rowView.findViewById(R.id.courtNameTv) as TextView
        val courtImage = rowView.findViewById(R.id.courtImage) as ImageView
        val courtLocation = rowView.findViewById(R.id.courtLocationTv) as TextView
        val courtRating = rowView.findViewById<TextView>(R.id.courtRatingTv)
        val courtBooking = rowView.findViewById<TextView>(R.id.numBookingTv)
        val courtDistance = rowView.findViewById<TextView>(R.id.distanceTv)
        val courtPrice = rowView.findViewById<TextView>(R.id.priceHomeTv)
        courtName.text = courtInfo[position].Name
        if(courtInfo[position].bitmapArrayList.size > 0)
            courtImage.setImageBitmap(courtInfo[position].bitmapArrayList[0])
        courtLocation.text = courtInfo[position].location!!.addressName
        courtRating.text = courtInfo[position].avgRating.toString() + " (${courtInfo[position].numRating} rate)"
        courtBooking.text = courtInfo[position].numBooking.toString() + " booking"
        courtDistance.text = GetDistance.formatNumber(courtInfo[position].courtDistance)
        courtPrice.text = formatPrice(courtInfo[position].Price)
        return rowView
    }
    fun formatPrice(price: Int): String {
        val formatter = java.text.DecimalFormat("#,###")
        return formatter.format(price) + "đ"
    }
}