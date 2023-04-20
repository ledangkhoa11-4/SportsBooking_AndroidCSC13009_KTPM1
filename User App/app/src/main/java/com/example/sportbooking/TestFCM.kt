package com.example.sportbooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.Constants.TAG
import com.google.firebase.messaging.FirebaseMessaging

class TestFCM : AppCompatActivity() {
    private lateinit var tokenET: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_fcm)

        tokenET = findViewById(R.id.tokenET)

        // Lấy token
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
//              Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                println("Fetching FCM registration token failed")
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
//            Log.d(TAG, msg)
            println(token)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            Toast.makeText(baseContext, "Your token is: $token", Toast.LENGTH_SHORT).show()

            tokenET.setText(token)
        })
    }
}