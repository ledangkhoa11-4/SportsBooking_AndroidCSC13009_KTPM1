package com.example.sportbooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MyFavoriteActivity : AppCompatActivity() {
    lateinit var listView:ListView
    lateinit var adapter: favoriteListViewAdapter
    lateinit var listFavoriteCourt:ArrayList<Court>
    lateinit var emptyImage:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_favorite)

        listView = findViewById(R.id.listFavoriteLV)
        listFavoriteCourt = ArrayList()
        adapter = favoriteListViewAdapter(this, listFavoriteCourt)
        listView.adapter = adapter
        listView.divider = null
        loadFavoriteList()
        listView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, DetailCourtActivity::class.java)
            val idx = MainActivity.listCourt.indexOf(listFavoriteCourt[i])
            intent.putExtra("index",idx)
            startActivity(intent)
        }
        findViewById<ImageButton>(R.id.backButtonRating).setOnClickListener {
            finish()
        }
        emptyImage = findViewById(R.id.EmptyFavorite)
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
                if(listFavoriteCourt.size == 0)
                    emptyImage.visibility = View.VISIBLE
                else
                    emptyImage.visibility = View.INVISIBLE
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}