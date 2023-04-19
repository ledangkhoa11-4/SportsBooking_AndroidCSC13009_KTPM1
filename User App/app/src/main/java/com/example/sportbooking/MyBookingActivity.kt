package com.example.sportbooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.viewpager2.widget.ViewPager2
import com.example.sportbooking.DTO.BookingHistory
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class MyBookingActivity : AppCompatActivity() {
    lateinit var nav_bar: NavigationBarView
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2
    lateinit var pagerAdapter: BookingPagerAdapter
    companion object{
        var bookingHistories: ArrayList<com.example.sportbooking.DTO.BookingHistory> = ArrayList()
        var incomingBooking:ArrayList<BookingHistory> = ArrayList()
        var finishBooking:ArrayList<BookingHistory> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_booking)
        loadBookingList()
        nav_bar = findViewById(R.id.nav_bar)
        navBarHandle(nav_bar)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewpager)
        pagerAdapter = BookingPagerAdapter(this)

        viewPager.adapter = pagerAdapter
        val tabLayoutMediator = TabLayoutMediator(tabLayout,viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when(position){
                    0-> tab.text = "Incoming"
                    1-> tab.text = "Completed"
                    2-> tab.text = "All booking"
                }
            })
        tabLayoutMediator.attach()

//        bookingListView = findViewById(R.id.bookingListView)
//        bookingListView.divider = null
//        bookingListViewAdapter = MyBookingListViewAdapter(this,bookingHistories)
//        bookingListView.adapter = bookingListViewAdapter
//
//        bookingListView.setOnItemClickListener { adapterView, view, i, l ->
//            val intent = Intent(this, DetailBookingHistory::class.java)
//            intent.putExtra("index",i)
//            startActivity(intent)
//        }
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
        bookingHistories.clear()
        incomingBooking.clear()
        finishBooking.clear()
        val bookingRef = MainActivity.database.getReference("Booking");
        val queryRef = bookingRef.orderByChild("userID").equalTo(MainActivity.user.id)
        queryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookingHistories.clear()
                incomingBooking.clear()
                finishBooking.clear()
                for(ds in snapshot.children){
                    val bookHistory = ds.getValue(BookingHistory::class.java)
                    bookHistory!!.ID = ds.key!!
                    bookingHistories.add(bookHistory!!)
                    val now = Date(System.currentTimeMillis())
                    val bookingTime = Date(bookHistory.Date)
                    if(bookingTime.after(now) && bookHistory.Status == false)
                        incomingBooking.add(bookHistory)
                    if(bookHistory.Status == true)
                        finishBooking.add(bookHistory)
                    val court = MainActivity.listCourt.filter { it.CourtID == bookHistory.CourtID }[0]
                    bookHistory.Court = court
                }
                if(AllBookingFragment.adapter != null)
                    AllBookingFragment.adapter!!.notifyDataSetChanged()
                if(IncomingBookingFragment.adapter != null)
                    IncomingBookingFragment.adapter!!.notifyDataSetChanged()
                if(FinishBookingFragment.adapter != null)
                    FinishBookingFragment.adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}