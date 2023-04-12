package com.example.sportbooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.example.sportbooking.DTO.BookingHistory
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MyBookingActivity : AppCompatActivity() {
    lateinit var nav_bar: NavigationBarView
    lateinit var bookingListView:ListView
    lateinit var bookingListViewAdapter: MyBookingListViewAdapter
    companion object{
        var bookingHistories: ArrayList<com.example.sportbooking.DTO.BookingHistory> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_booking)
        loadBookingList()
        nav_bar = findViewById(R.id.nav_bar)
        navBarHandle(nav_bar)

        bookingListView = findViewById(R.id.bookingListView)
        bookingListView.divider = null
        bookingListViewAdapter = MyBookingListViewAdapter(this,bookingHistories)
        bookingListView.adapter = bookingListViewAdapter

        bookingListView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, DetailBookingHistory::class.java)
            intent.putExtra("index",i)
            startActivity(intent)
        }
    }
    fun navBarHandle(nav_bar: NavigationBarView){
        nav_bar.selectedItemId = R.id.item_schedule
        nav_bar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_home-> {
                    startActivity(Intent(this,HomeActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.item_user->{
                    startActivity(Intent(this,UserTabActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.item_schedule-> true
                else -> {
                    false
                }
            }
        }
    }
    fun loadBookingList(){
        val bookingRef = MainActivity.database.getReference("Booking");
        val queryRef = bookingRef.orderByChild("UserID").equalTo("daylamotuserid")
        queryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookingHistories.clear()
                for(ds in snapshot.children){
                    val bookHistory = ds.getValue(BookingHistory::class.java)
                    bookingHistories.add(bookHistory!!)
                    Log.i("AAAAAAAAAAAA",MainActivity.listCourt.size.toString())
                    val court = MainActivity.listCourt.filter { it.CourtID == bookHistory.CourtID }[0]
                    bookHistory.Court = court
                }
                bookingListViewAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}