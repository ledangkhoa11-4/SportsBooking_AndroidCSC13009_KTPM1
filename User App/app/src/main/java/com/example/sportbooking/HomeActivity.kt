package com.example.sportbooking

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.ListView
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.navigation.NavigationBarView

class HomeActivity : AppCompatActivity() {
    lateinit var nav_bar: NavigationBarView
    lateinit var listView: ListView
    lateinit var locationManager: com.example.sportbooking.LocationManager
    companion object{
        var listViewAdapter:homeListViewAdapter? = null
        var courtList_Home:ArrayList<Court>? = null
        fun updateDistance(){
            for(court in courtList_Home!!){
                if(MainActivity.lastLocation.latLng.latitude != 0.0 && MainActivity.lastLocation.latLng.longitude != 0.0){
                    court.courtDistance = GetDistance.getDistance(MainActivity.lastLocation,court.location!!).toDouble()
                }
            }
            listViewAdapter?.notifyDataSetChanged()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        nav_bar = findViewById(R.id.nav_bar)
        navBarHandle(nav_bar)
        locationManager = LocationManager(this);
        locationManager.startLocationUpdates()
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
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationManager.handleOnRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}