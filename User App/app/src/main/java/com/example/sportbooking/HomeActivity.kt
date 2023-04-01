package com.example.sportbooking

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.navigation.NavigationBarView

class HomeActivity : AppCompatActivity() {
    lateinit var nav_bar: NavigationBarView
    lateinit var listView: ListView
    companion object{
        var listViewAdapter:homeListViewAdapter? = null
        var courtList_Home:ArrayList<Court>? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        nav_bar = findViewById(R.id.nav_bar)
        navBarHandle(nav_bar)
        listView = findViewById(R.id.homeListView)
        courtList_Home = MainActivity.listCourt

        listViewAdapter = homeListViewAdapter(this,courtList_Home!!)
        listView.adapter = listViewAdapter
        listView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, DetailCourtActivity::class.java)
            intent.putExtra("index",i)
            startActivity(intent)
        }
    }

    fun navBarHandle(nav_bar: NavigationBarView){
        nav_bar.selectedItemId = R.id.item_home
        nav_bar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_home-> true
                R.id.item_user->{

                    startActivity(Intent(this,UserTabActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}