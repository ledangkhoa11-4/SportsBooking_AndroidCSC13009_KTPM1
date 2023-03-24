package com.example.sportbooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.navigation.NavigationBarView

class UserTabActivity : AppCompatActivity() {
    lateinit var nav_bar: NavigationBarView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_tab)
        nav_bar = findViewById(R.id.nav_bar)
        navBarHandle(nav_bar)

    }
    fun navBarHandle(nav_bar: NavigationBarView){
        nav_bar.selectedItemId = R.id.item_user
        nav_bar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_home->{
                    startActivity(Intent(this,MainActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                R.id.item_user->{
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}