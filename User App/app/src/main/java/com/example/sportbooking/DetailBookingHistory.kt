package com.example.sportbooking

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import net.glxn.qrgen.android.QRCode
import java.text.SimpleDateFormat
import java.util.*

class DetailBookingHistory : AppCompatActivity() {
    lateinit var viewPager: ViewPager2
    lateinit var dot: DotsIndicator
    lateinit var detailBooking:com.example.sportbooking.DTO.BookingHistory

    lateinit var courtName:TextView
    lateinit var bookingTime:TextView
    lateinit var locationTv:TextView
    lateinit var priceRent:TextView
    lateinit var qrCode:ImageView
    lateinit var status:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_booking_history)

        courtName = findViewById(R.id.courtNameDetailBooking)
        bookingTime = findViewById(R.id.datetimeDetailBooking)
        locationTv = findViewById(R.id.courtLocationDetailBooking)
        priceRent = findViewById(R.id.rentPriceDetailBooking)
        qrCode = findViewById(R.id.qrImage)
        status = findViewById(R.id.statusBookingDetailTv)

        val intent = intent
        val index = intent.getIntExtra("index",0)
        detailBooking = MyBookingActivity.bookingHistories[index]
        viewPager = findViewById(R.id.listImageDetailVP)
        dot = findViewById(R.id.dots_indicator)
        waitStatusChange()

        val vpAdapter = ImageViewPagerAdapter(detailBooking.Court!!.bitmapArrayList)
        viewPager.setPageTransformer(MarginPageTransformer(37));
        viewPager.adapter = vpAdapter
        dot.attachTo(viewPager)

        courtName.text = detailBooking!!.Court!!.Name
        bookingTime.text = "${convertTime(detailBooking.Time[0])} - ${convertTime(detailBooking.Time[1])}, ${convertDate(detailBooking.Date)} "
        priceRent.text = formatPrice(detailBooking.TotalPrice)

        if(detailBooking.Status == true){
            status.setText("Checked-in successfully")
            status.setTextColor(Color.parseColor("#02b002"))
        }else status.setText("Not check-in yet")

        val myBitmap = QRCode.from(detailBooking.SecretID.toString()).bitmap()
        qrCode.setImageBitmap(myBitmap)

        val enlargeQR: View = LayoutInflater.from(this).inflate(R.layout.enlarge_qrcode,null);
        enlargeQR.findViewById<ImageView>(R.id.qrCodeImage).setImageBitmap(myBitmap)
        qrCode.setOnClickListener {
            if(enlargeQR.parent != null){
                var vg = enlargeQR.parent as ViewGroup
                vg.removeView(enlargeQR)
            }
            MaterialAlertDialogBuilder(this)
                .setTitle(" ")
                .setView(enlargeQR)
                .setPositiveButton("Close") { dialog, which ->

                }
                .show()
        }
        findViewById<ImageButton>(R.id.backButtonRating).setOnClickListener {
            finish()
        }
        findViewById<Button>(R.id.ratingButton).setOnClickListener {
            val rating = Intent(this, RatingActivity::class.java)
            rating.putExtra("index",index)
            startActivity(rating)
        }
    }
    fun convertTime(timeStamp:Long):String{
        val sdf = SimpleDateFormat("HH:mm") // create a SimpleDateFormat object with desired format
        val formattedTime = sdf.format(Date(timeStamp)) // convert timestamp to date and format as string
        return formattedTime
    }
    fun convertDate(timeStamp:Long):String{
        val sdf = SimpleDateFormat("MMMM dd, yyyy") // create a SimpleDateFormat object with desired format
        val formattedTime = sdf.format(Date(timeStamp)) // convert timestamp to date and format as string
        return formattedTime
    }
    fun formatPrice(price: Int): String {
        val formatter = java.text.DecimalFormat("#,###")
        return "Paid:" + formatter.format(price) + "đ"
    }
    fun waitStatusChange(){
        val bookingRef = MainActivity.database.getReference("Booking")
        val query = bookingRef.orderByChild("SecretID").equalTo(detailBooking.SecretID.toDouble())
        query.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    bookingRef.child(ds.key!!).child("Status").get().addOnSuccessListener {
                        val newStatus:Boolean? = it.getValue(Boolean::class.java)
                        detailBooking.Status = newStatus!!
                        if(detailBooking.Status == true){
                            status.setText("Checked-in successfully")
                            status.setTextColor(Color.parseColor("#02b002"))
                            CreateToast.createToast(this@DetailBookingHistory,"Successfully","Checked-in successfullt, have fun!",true)
                        }else status.setText("Not check-in yet")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}