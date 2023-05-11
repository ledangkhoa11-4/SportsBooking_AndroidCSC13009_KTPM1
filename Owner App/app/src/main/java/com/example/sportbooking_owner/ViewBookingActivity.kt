package com.example.sportbooking_owner

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.viewpager2.widget.ViewPager2
import com.example.sportbooking_owner.Adapter.BookingPagerAdapter
import com.example.sportbooking_owner.DTO.BookingHistory
import com.example.sportbooking_owner.DTO.Courts
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.Date

class ViewBookingActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2
    lateinit var pagerAdapter: BookingPagerAdapter
    lateinit var court:Courts
    companion object{
        var listIncoming:ArrayList<BookingHistory> = ArrayList()
        var listFinished:ArrayList<BookingHistory> = ArrayList()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_booking)

        val courtpos = intent.getIntExtra("pos", 0)
        court = SignIn.listCourt[courtpos]

        loadListBooking()

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewpager)
        pagerAdapter = BookingPagerAdapter(this)

        viewPager.adapter = pagerAdapter
        val tabLayoutMediator = TabLayoutMediator(tabLayout,viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when(position){
                    0-> tab.text = "Incoming Booking"
                    1-> tab.text = "Completed Booking"
                }
            })
        tabLayoutMediator.attach()

        findViewById<ImageButton>(R.id.backButtonBookingView).setOnClickListener {
            finish()
        }
    }

    fun loadListBooking(){
        listIncoming.clear()
        listFinished.clear()
        val bookingRef = MainActivity.database.getReference("Booking")
        val query = bookingRef.orderByChild("courtID").equalTo(court.CourtID)
        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listIncoming.clear()
                listFinished.clear()
                for(ds in snapshot.children){
                    val bookHistory = ds.getValue(BookingHistory::class.java)
                    bookHistory!!.Court = court
                    val customerID =bookHistory!!.UserID

                    val userquery = MainActivity.database.getReference("User").orderByChild("id").equalTo(customerID)
                    userquery.addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for(userDs in snapshot.children){
                               val nameQuery = userDs.child("username").getValue(String::class.java)
                                if(nameQuery != null){
                                    bookHistory.CustomerName = nameQuery
                                    if(IncomingBookingFragment.adapter != null)
                                        IncomingBookingFragment.adapter!!.notifyDataSetChanged()
                                    if(FinishBookingFragment.adapter != null)
                                        FinishBookingFragment.adapter!!.notifyDataSetChanged()
                                }

                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                    var imageRef = MainActivity.storageRef.child("user${bookHistory.UserID}")
                    val ONE_MEGABYTE: Long = 1024 * 1024 * 4
                    imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                        bookHistory.CustomerImage = bitmap
                        if(IncomingBookingFragment.adapter != null)
                            IncomingBookingFragment.adapter!!.notifyDataSetChanged()
                        if(FinishBookingFragment.adapter != null)
                            FinishBookingFragment.adapter!!.notifyDataSetChanged()
                    }.addOnFailureListener {
                        // Handle any errors
                    }
                    var today = Date(System.currentTimeMillis())
                    var bookingDate = Date(bookHistory.Date)
                    if(bookHistory.Status == false && bookingDate.after(today)){
                        listIncoming.add(bookHistory)
                    }else{
                        listFinished.add(bookHistory)
                    }
                    if(IncomingBookingFragment.adapter != null)
                        IncomingBookingFragment.adapter!!.notifyDataSetChanged()
                    if(FinishBookingFragment.adapter != null)
                        FinishBookingFragment.adapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}