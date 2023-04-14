package com.example.sportbooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MyFavoriteActivity : AppCompatActivity() {
    lateinit var listView:ListView
    lateinit var adapter: homeListViewAdapter
    lateinit var listFavoriteCourt:ArrayList<Court>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_favorite)

        listView = findViewById(R.id.listFavoriteLV)
        listFavoriteCourt = ArrayList()
        adapter = homeListViewAdapter(this, listFavoriteCourt)
        listView.adapter = adapter
        listView.divider = null
        loadFavoriteList()
    }

    fun loadFavoriteList(){
        val favoriteRef = MainActivity.database.getReference("Favorite")
        val queryRef = favoriteRef.orderByChild("id").equalTo(MainActivity.user.id)
        queryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listFavoriteCourt.clear()
                for(ds in snapshot.children){
                    val courtID = ds.child("courtID").getValue(String::class.java)
                    val courts = MainActivity.listCourt.filter { it.CourtID == courtID }
                    listFavoriteCourt.addAll(courts)
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}