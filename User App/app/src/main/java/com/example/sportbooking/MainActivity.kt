package com.example.sportbooking

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class MainActivity : AppCompatActivity() {
    lateinit var nav_bar:NavigationBarView
    companion object {
        val database = Firebase.database("https://sportbooking2-b3fa8-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val storageRef = Firebase.storage.getReferenceFromUrl("gs://sportbooking2-b3fa8.appspot.com")
        var listCourt:ArrayList<Court>  = ArrayList()

        val GG_MAP_API = "" //Vì api free nên hay bị hết lược :((
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("RÊNW","AAAAAAAAAA")
        loadCourtList()
        finish()
        // startActivity(Intent(this,CalendarViewActivity::class.java))
       // startActivity(Intent(this,RatingActivity::class.java))
        startActivity(Intent(this,HomeActivity::class.java))
        //startActivity(Intent(this,SearchStadiumActivity::class.java))
        //startActivity(Intent(this,CourtScheduleActivity::class.java))
        //startActivity(Intent(this,CalendarViewActivity::class.java))


    }

    private fun loadCourtList() {
        var courtsRef = database.getReference("Courts");
        var valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val courtDbId: String = ds.key!!
                    val courtRef: DatabaseReference = database.getReference().child("Courts").child(courtDbId)
                    val eventListener: ValueEventListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val court: Court? = dataSnapshot.getValue(Court::class.java)
                            for (imageName in court!!.Images){
                                var imageRef = storageRef.child(imageName)
                                val ONE_MEGABYTE: Long = 1024 * 1024
                                imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                                    val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                                    court.bitmapArrayList.add(bitmap)
                                    if(HomeActivity.listViewAdapter != null){
                                        HomeActivity.listViewAdapter!!.notifyDataSetChanged()
                                    }
                                }.addOnFailureListener {
                                    // Handle any errors
                                }
                            }

                            listCourt.add(court)
                            Log.i("AAAAAAAAAAA", listCourt.size.toString())
                            if(HomeActivity.listViewAdapter != null){
                                HomeActivity.listViewAdapter!!.notifyDataSetChanged()
                            }

                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    }
                    courtRef.addListenerForSingleValueEvent(eventListener)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        courtsRef.addValueEventListener(valueEventListener)
    }

}