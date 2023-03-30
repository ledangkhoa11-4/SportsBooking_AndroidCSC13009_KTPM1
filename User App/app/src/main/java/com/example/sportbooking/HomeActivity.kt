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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        nav_bar = findViewById(R.id.nav_bar)
        navBarHandle(nav_bar)
        listView = findViewById(R.id.homeListView)
        var courtList = ArrayList<Court>()
        val drawable = resources.getDrawable(R.drawable.san_bong)
        val bitmap = (drawable as BitmapDrawable).bitmap

        val court = Court(1,1,"Sân bóng Trí Hải", "227 Đ.Nguyễn Văn Cừ, Phường 4, Quận 5, Hồ Chí Minh",
            "Bóng đá", "", ArrayList(), arrayListOf(bitmap),ArrayList(),"",
            ArrayList(),50000 , 2.7,25,90,500.0
        )
        courtList.add(court)
        courtList.add(court)
        courtList.add(court)
        courtList.add(court)
        courtList.add(court)
        courtList.add(court)
        courtList.add(court)
        courtList.add(court)
        val listViewAdapter = homeListViewAdapter(this,courtList)
        listView.adapter = listViewAdapter
        listView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, DetailCourtActivity::class.java)
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
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}