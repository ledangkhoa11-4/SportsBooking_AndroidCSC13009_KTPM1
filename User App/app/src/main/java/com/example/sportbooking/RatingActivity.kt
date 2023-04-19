package com.example.sportbooking

import android.media.Rating
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.sportbooking.DTO.RatingCourt
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToast.Companion.createToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.Math.floor
import java.util.Objects

class RatingActivity : AppCompatActivity() {
    lateinit var detailBooking:com.example.sportbooking.DTO.BookingHistory
    lateinit var viewPager: ViewPager2
    lateinit var dot: DotsIndicator
    lateinit var fieldName:TextView
    lateinit var ratingBar1: RatingBar
    lateinit var ratingBar2: RatingBar
    lateinit var commitButton: Button
    lateinit var comment: EditText
    var count: Int = 0
    var listRating: ArrayList<RatingCourt> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)
        val intent = intent
        val index = intent.getIntExtra("index",0)
        detailBooking = MyBookingActivity.bookingHistories[index]
        loadRatingList()
        viewPager = findViewById(R.id.listImageDetailVP)
        dot = findViewById(R.id.dots_indicator)
        fieldName = findViewById(R.id.fieldName)
        ratingBar1 = findViewById(R.id.ratingbar1)
        ratingBar2 = findViewById(R.id.ratingbar2)
        commitButton = findViewById(R.id.commit)
        comment = findViewById(R.id.commentEditText)
        fieldName.text = detailBooking!!.Court!!.Name
        val vpAdapter = ImageViewPagerAdapter(detailBooking.Court!!.bitmapArrayList)
        viewPager.setPageTransformer(MarginPageTransformer(37));
        viewPager.adapter = vpAdapter
        dot.attachTo(viewPager)
        val b1 = findViewById<Button>(R.id.button1)
        val b2 = findViewById<Button>(R.id.button2)
        val b3 = findViewById<Button>(R.id.button3)
        val b4 = findViewById<Button>(R.id.button4)
        val b5 = findViewById<Button>(R.id.button5)
        val b6 = findViewById<Button>(R.id.button6)
        val isTrue = arrayListOf<Boolean>(false,false,false,false,false,false)
        b1.setOnClickListener{
            if(isTrue[0] == false){
                b1.setBackgroundColor(ContextCompat.getColor(this,R.color.topic_color))
                isTrue[0] = true
            }
            else{
                b1.setBackgroundColor(ContextCompat.getColor(this,R.color.beautiful_gray))
                isTrue[0] = false
            }
        }
        b2.setOnClickListener{
            if(isTrue[1] == false){
                b2.setBackgroundColor(ContextCompat.getColor(this,R.color.topic_color))
                isTrue[1] = true
            }
            else{
                b2.setBackgroundColor(ContextCompat.getColor(this,R.color.beautiful_gray))
                isTrue[1] = false
            }
        }
        b3.setOnClickListener{
            if(isTrue[2] == false){
                b3.setBackgroundColor(ContextCompat.getColor(this,R.color.topic_color))
                isTrue[2] = true
            }
            else{
                b3.setBackgroundColor(ContextCompat.getColor(this,R.color.beautiful_gray))
                isTrue[2] = false
            }
        }
        b4.setOnClickListener{
            if(isTrue[3] == false){
                b4.setBackgroundColor(ContextCompat.getColor(this,R.color.topic_color))
                isTrue[3] = true
            }
            else{
                b4.setBackgroundColor(ContextCompat.getColor(this,R.color.beautiful_gray))
                isTrue[3] = false
            }
        }
        b5.setOnClickListener{
            if(isTrue[4] == false){
                b5.setBackgroundColor(ContextCompat.getColor(this,R.color.topic_color))
                isTrue[4] = true
            }
            else{
                b5.setBackgroundColor(ContextCompat.getColor(this,R.color.beautiful_gray))
                isTrue[4] = false
            }
        }
        b6.setOnClickListener{
            if(isTrue[5] == false){
                b6.setBackgroundColor(ContextCompat.getColor(this,R.color.topic_color))
                isTrue[5] = true
            }
            else{
                b6.setBackgroundColor(ContextCompat.getColor(this,R.color.beautiful_gray))
                isTrue[5] = false
            }
        }
        commitButton.setOnClickListener {
            if(ratingBar2.rating == 0.0f){
                createToast("Sorry","Please rating before submit", false);
                return@setOnClickListener
            }
            else{
                val ratingRef = MainActivity.database.getReference("Rating")
                val rating = com.example.sportbooking.DTO.RatingCourt(MainActivity.user.id,detailBooking!!.ID,detailBooking!!.CourtID,
                    MainActivity.user.username,null,ratingBar2.rating,
                    comment.text.toString(),isTrue)
                ratingRef.push().setValue(rating)
                val courtRef = MainActivity.database.getReference("Courts")
                val queryRef = courtRef.orderByChild("courtID").equalTo(detailBooking.CourtID)
                queryRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(ds in snapshot.children){

                            if(ds.child("courtID").getValue(String::class.java).equals(detailBooking.CourtID)){
                                val keyRef = ds.key
                                courtRef.child(keyRef!!).child("avgRating").setValue(ratingBar1.rating)
                                courtRef.child(keyRef!!).child("numRating").setValue(count)
                                break
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
                createToast("Rate success","Thank you for choosing us",true )

            }
        }
        findViewById<ImageButton>(R.id.backButtonRating).setOnClickListener {
            finish()
        }
    }

    fun loadRatingList() {
        val ratingRef = MainActivity.database.getReference("Rating");
        val queryRef = ratingRef.orderByChild("courtID").equalTo(detailBooking.CourtID)
        queryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listRating.clear()
                count = 0
                var sum = 0.0f
                for (ds in dataSnapshot.children) {
                    val rating = ds.getValue(RatingCourt::class.java)
                    listRating.add(rating!!)
                    sum += rating.star!!
                    count++
                }
                var ratingResult = sum/listRating.size
                ratingBar1.rating = ratingResult
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun createToast(title:String, message:String, isSuccess:Boolean){
        var style: MotionToastStyle
        if(isSuccess)
            style = MotionToastStyle.SUCCESS
        else
            style = MotionToastStyle.ERROR
        MotionToast.createToast(this,
            title,
            message,
            style,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
    }

}