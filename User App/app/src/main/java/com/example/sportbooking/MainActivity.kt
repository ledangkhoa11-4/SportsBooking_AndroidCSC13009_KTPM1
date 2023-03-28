package com.example.sportbooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.navigation.NavigationBarMenu
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    lateinit var nav_bar:NavigationBarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        nav_bar = findViewById(R.id.nav_bar)
//        navBarHandle(nav_bar)
        startActivity(Intent(this,CourtScheduleActivity::class.java))
    }
    fun navBarHandle(nav_bar: NavigationBarView){
        nav_bar.selectedItemId = R.id.item_home
        nav_bar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_home-> true
                R.id.item_user->{
                    startActivity(Intent(this,UserTabActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}