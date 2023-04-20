package com.example.sportbooking

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class NotificationActivity : AppCompatActivity() {
    private var customListView: ListView? = null
    private var primaryNoti = arrayListOf<String>()
    private var secondaryNoti = arrayListOf<String>()
    private var imageNoti = arrayListOf<Int>()

    fun mockData() {
        primaryNoti = arrayListOf("Bạn đã đặt sân thành công", "Vui lòng thanh toán tiền sân")
        secondaryNoti = arrayListOf("Sân bóng 5 người", "Sân bóng 7 người")
        imageNoti = arrayListOf(R.drawable.check, R.drawable.check)
    }

    // create a notification
//    val CHANNEL_ID = "channel_id_example_01"
//    val myBitmap = BitmapFactory.decodeResource(resources, androidx.core.R.drawable.notification_icon_background)
//    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
//        .setSmallIcon(androidx.legacy.v4.R.drawable.notification_icon_background)
//        .setLargeIcon(myBitmap)
//        .setContentTitle("My notification")
//        .setContentText("Much longer text that cannot fit one line...")
//        .setStyle(NotificationCompat.BigTextStyle()
//            .bigText("Much longer text that cannot fit one line..." + "\nMuch longer text that cannot fit one line..."))
//        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = getString(R.string.channel_name)
//            val descriptionText = getString(R.string.channel_description)
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(CHANNEL_ID, name, importance)
//                .apply {
//                    description = descriptionText
//                }
//            val notificationManager: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as
//                        NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        customListView = findViewById(R.id.notificationLV)

        mockData()

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