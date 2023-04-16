package com.example.sportbooking_owner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class MainActivity : AppCompatActivity() {
    companion object {
        val database = Firebase.database("https://sportbooking2-b3fa8-default-rtdb.asia-southeast1.firebasedatabase.app")
        val storageRef = Firebase.storage.getReferenceFromUrl("gs://sportbooking2-b3fa8.appspot.com")
        val apiPlace = "AIzaSyB352MaQT56jsnR1N4mDqPUEh3GPEhiRvE"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //startActivity(Intent(this, NewInfoActivity::class.java))
        startActivity(Intent(this, SignIn::class.java))
        finish()
        //startActivity(Intent(this, CourtScheduleActivity::class.java))
        //startActivity(Intent(this,CourtListActivity::class.java))
    }
}