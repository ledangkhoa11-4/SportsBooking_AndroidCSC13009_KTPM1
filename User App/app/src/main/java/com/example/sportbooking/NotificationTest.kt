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
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.*

class NotificationTest : AppCompatActivity() {
    private lateinit var notificationBtn: Button
    private lateinit var myNotificationManager: NotificationManagerCompat
    private lateinit var myNotificationChannel: NotificationChannel
    private lateinit var myNotificationBuilder: NotificationCompat.Builder

    private var notificationId = 0

    // Tạo các channel id
    private companion object {
        private const val CHANNEL_ID = "channel_01"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_test)

//        Log.d("NotificationTest", intent.getStringExtra("key1").toString()

        notificationBtn = findViewById(R.id.notiBtn)
        notificationBtn.setOnClickListener {
            showNotification()
        }
    }

    private fun showNotification() {
        createNotificationChannel()

        val date = Date()
//        val notificationId = SimpleDateFormat("ddHmmss", Locale.US).format(date).toInt()
//        val notificationId = 0

        // Chạy intent mới
        val intent = Intent(this, NotificationTest::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        // Trả về giá trị cho intent mới
        intent.putExtra("key1", "value1")
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Tạo ra 1 notification builder
        myNotificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.navigation_icon)
            .setContentTitle("Sport Booking")
            .setContentText("Bạn có 1 lịch đặt sân mới")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // max: cực quan trọng, high: quan trọng (các tin nhắn), default: bình thường, low: ít quan trọng
            .setAutoCancel(true) // tự động đóng notification sau khi click vào
            .setContentIntent(pendingIntent) // chạy intent mới ở trên

        // Tạo ra 1 notification manager
        myNotificationManager = NotificationManagerCompat.from(this)
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
        myNotificationManager.notify(notificationId, myNotificationBuilder.build()) // Id dùng để xác định notification

    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            myNotificationChannel = NotificationChannel(CHANNEL_ID, name, importance)
                .apply {
                    description = descriptionText
                }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as
                        NotificationManager
            notificationManager.createNotificationChannel(myNotificationChannel)
        }
    }
}