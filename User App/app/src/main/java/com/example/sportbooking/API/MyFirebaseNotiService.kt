package com.example.sportbooking.API

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.sportbooking.MainActivity
import com.example.sportbooking.MyBookingActivity
import com.example.sportbooking.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelID = "notification_channel"
const val channelName = "com.example.sportbooking"
class MyFirebaseNotiService : FirebaseMessagingService() {
    //generate notification

    //show the notification
    override fun onMessageReceived(message: RemoteMessage) {
        Log.i("AAAAAAAAAAAAAAAA",message.notification!!.title!!)
        Log.i("AAAAAAAAAAAAAAAA",message.notification!!.body!!)
        if(message.notification != null)
        {
            generateNoti(message.notification!!.title!!, message.notification!!.body!!)
        }

    }
    fun generateNoti(title: String, des: String) {
        val intent = Intent(applicationContext, MyBookingActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT
        )
        //channel id, channel name
        var builder = NotificationCompat.Builder(applicationContext,channelID)
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(Notification.DEFAULT_ALL)
        builder = builder.setContent(getRemoteView(title, des))

        val notiManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelID, channelName,NotificationManager.IMPORTANCE_HIGH)
            notiManager.createNotificationChannel(notificationChannel)
        }
        notiManager.notify(0,builder.build() )
    }

    fun getRemoteView(title:String, des:String):RemoteViews{
        val remoteView = RemoteViews(packageName, R.layout.notification_layout)
        remoteView.setTextViewText(R.id.titleNoti, title)
        remoteView.setTextViewText(R.id.desNoti, des)
        return remoteView
    }
}