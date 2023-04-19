package com.example.sportbooking

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.*

class NotificationTest : AppCompatActivity() {
    private lateinit var notificationBtn: Button

    // Tạo các channel id
    private companion object {
        private const val CHANNEL_ID = "channel_01"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_test)

        notificationBtn = findViewById(R.id.notiBtn)
        notificationBtn.setOnClickListener {
            showNotification()
        }
    }

    private fun showNotification() {
        createNotificationChannel()

        val date = Date()
        val notificationId = SimpleDateFormat("ddHmmss", Locale.US).format(date).toInt()

        // Chạy intent mới
        val intent = Intent(this, NotificationTest::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)


        // Tạo ra 1 notification builder
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.navigation_icon)
            .setContentTitle("Sport Booking")
            .setContentText("Bạn có 1 lịch đặt sân mới")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // max: cực quan trọng, high: quan trọng (các tin nhắn), default: bình thường, low: ít quan trọng
            .setAutoCancel(true) // tự động đóng notification sau khi click vào
            .setContentIntent(pendingIntent) // chạy intent mới ở trên

        // Tạo ra 1 notification manager
        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(notificationId, notificationBuilder.build())

    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
                .apply {
                    description = descriptionText
                }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as
                        NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}