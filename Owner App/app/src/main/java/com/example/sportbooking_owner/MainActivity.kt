package com.example.sportbooking_owner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //startActivity(Intent(this, NewInfoActivity::class.java))
        //startActivity(Intent(this, SignIn::class.java))
        //startActivity(Intent(this, CourtScheduleActivity::class.java))
        //startActivity(Intent(this,CourtListActivity::class.java))\


        val database = Firebase.database("https://sportbooking2-b3fa8-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("message")
        myRef.setValue("Hello, World!")
    }
}