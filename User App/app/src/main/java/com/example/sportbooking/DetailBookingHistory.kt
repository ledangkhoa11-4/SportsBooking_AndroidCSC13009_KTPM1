package com.example.sportbooking

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        findViewById<ImageButton>(R.id.backButtonBookingDetail).setOnClickListener {
            finish()
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
}