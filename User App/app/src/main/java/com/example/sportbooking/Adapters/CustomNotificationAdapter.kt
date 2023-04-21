package com.example.sportbooking.Adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.sportbooking.R

class CustomNotificationAdapter(
    private val context: Activity, private val primaryNoti: List<String>,
    private val secondaryNoti: List<String>, private val images: List<Int>
) : ArrayAdapter<String>(context, R.layout.notification_item_layout, primaryNoti) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView: View = inflater.inflate(R.layout.notification_item_layout, null, true)

        val primaryNotificationTV = rowView.findViewById(R.id.PrimaryNotificationTV) as TextView
        val secondaryNotificationTV = rowView.findViewById(R.id.SecondaryNotificationTV) as TextView
        val statusNotificationIV = rowView.findViewById(R.id.statusNotificationIV) as ImageView

        primaryNotificationTV.text = primaryNoti[position]
        secondaryNotificationTV.text = secondaryNoti[position]
        statusNotificationIV.setImageResource(images[position])

        return rowView
    }

}
