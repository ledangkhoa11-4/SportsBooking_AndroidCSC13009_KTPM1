package com.example.sportbooking

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.AsyncTask
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
import java.io.*


class MainActivity : AppCompatActivity() {
    lateinit var nav_bar: NavigationBarView


    companion object {
        val database =
            Firebase.database("https://sportbooking2-b3fa8-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val storageRef =
            Firebase.storage.getReferenceFromUrl("gs://sportbooking2-b3fa8.appspot.com")
        var listCourt: ArrayList<Court> = ArrayList()
        var lastLocation:Location = Location()
        val GG_MAP_API =
            "AIzaSyAU_lL7tnCK2WX35eqamvlTVYlFjp-hq5Y" //Vì api free nên hay bị hết lược :((
        fun readLastLocation(context: Context): Location {
            var loc= Location()
            try {
                val inputStream: InputStream? = context.openFileInput("last_location.txt")
                if (inputStream != null) {

                    val inputStreamReader = InputStreamReader(inputStream)
                    val reader = BufferedReader(inputStreamReader)
                    var line : String? = reader.readLine()
                    val latlng = line!!.split(",")
                    loc.latLng.latitude = latlng[0].toDouble()
                    loc.latLng.longitude = latlng[1].toDouble()
                    inputStream.close()
                }
            } catch (e: java.lang.Exception) {

            }
            return loc
        }
        fun saveLocation(loc:Location, context: Context){
            try {
                val out = OutputStreamWriter(context.openFileOutput("last_location.txt", 0))
                out.write("${loc.latLng.latitude},${loc.latLng.longitude}")
                out.close()
            } catch (t: Throwable) {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadCourtList()
        lastLocation = readLastLocation(this);
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
        // startActivity(Intent(this,CalendarViewActivity::class.java))
        // startActivity(Intent(this,RatingActivity::class.java))

        //startActivity(Intent(this,SearchStadiumActivity::class.java))
        //startActivity(Intent(this,CourtScheduleActivity::class.java))
        //startActivity(Intent(this,CalendarViewActivity::class.java))
    }

    private fun loadCourtList() {
        var courtsRef = database.getReference("Courts");
        var valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listCourt.clear()
                for (ds in dataSnapshot.children) {
                    val courtDbId: String = ds.key!!
                    val courtRef: DatabaseReference =
                        database.getReference().child("Courts").child(courtDbId)
                    val eventListener: ValueEventListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val court: Court? = dataSnapshot.getValue(Court::class.java)
                            for (imageName in court!!.Images) {
                                var imageRef = storageRef.child(imageName)
                                val ONE_MEGABYTE: Long = 1024 * 1024
                                imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                                    val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                                    court.bitmapArrayList.add(bitmap)
                                    if (HomeActivity.listViewAdapter != null) {
                                        HomeActivity.listViewAdapter!!.notifyDataSetChanged()
                                    }
                                }.addOnFailureListener {
                                    // Handle any errors
                                }
                            }
                            if(MainActivity.lastLocation.latLng.latitude != 0.0 && MainActivity.lastLocation.latLng.longitude != 0.0){
                                court.courtDistance = GetDistance.getDistance(MainActivity.lastLocation,court.location!!).toDouble()
                            }

                            listCourt.add(court)
                            if (HomeActivity.listViewAdapter != null) {
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