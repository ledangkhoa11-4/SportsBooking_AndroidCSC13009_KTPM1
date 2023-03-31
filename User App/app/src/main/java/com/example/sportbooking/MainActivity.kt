package com.example.sportbooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.navigation.NavigationBarMenu
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var nav_bar:NavigationBarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // startActivity(Intent(this,CalendarViewActivity::class.java))
        startActivity(Intent(this,RatingActivity::class.java))
//        startActivity(Intent(this,HomeActivity::class.java))
        //startActivity(Intent(this,SearchStadiumActivity::class.java))
        //startActivity(Intent(this,CourtScheduleActivity::class.java))
        //startActivity(Intent(this,CalendarViewActivity::class.java))



        /*TEst database*/

        // Write a message to the database
        val database = Firebase.database("https://sportbooking2-b3fa8-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("message")
        myRef.setValue("Hello, ABC!")
    }

}