package com.example.sportbooking

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView

class NotificationActivity : AppCompatActivity() {
    private var customListView: ListView? = null
    private var primaryNoti = arrayListOf<String>()
    private var secondaryNoti = arrayListOf<String>()
    private var imageNoti = arrayListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        customListView = findViewById(R.id.notificationLV)

        val adapter = CustomNotificationAdapter(this, primaryNoti, secondaryNoti, imageNoti)
        customListView!!.adapter = adapter

        customListView!!.setOnItemClickListener { adapterView, view, i, l ->
            val primaryNotificationTV = view.findViewById(R.id.PrimaryNotificationTV) as TextView
            val secondaryNotificationTV = view.findViewById(R.id.SecondaryNotificationTV) as TextView
            val statusNotificationIV = view.findViewById(R.id.statusNotificationIV) as ImageView

            primaryNotificationTV.text = primaryNoti[i]
            secondaryNotificationTV.text = secondaryNoti[i]
            statusNotificationIV.setImageResource(imageNoti[i])
        }

    }
}