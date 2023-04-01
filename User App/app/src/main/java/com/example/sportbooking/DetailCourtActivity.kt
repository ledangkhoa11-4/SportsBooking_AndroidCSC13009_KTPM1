package com.example.sportbooking

import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class DetailCourtActivity : AppCompatActivity() {
    lateinit var viewPager:ViewPager2
    lateinit var dot:DotsIndicator

    lateinit var typeSportImage:ImageView;
    lateinit var courtName:TextView
    lateinit var courtLocation:TextView
    lateinit var courtPhone:TextView
    lateinit var courtPrice:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_court)

        val intent = intent
        val index = intent.getIntExtra("index",0)
        val courtDetail = HomeActivity.courtList_Home!![index]
        viewPager = findViewById(R.id.listImageDetailVP)
        dot = findViewById(R.id.dots_indicator)



        val vpAdapter = ImageViewPagerAdapter(courtDetail.bitmapArrayList)
        viewPager.setPageTransformer(MarginPageTransformer(37));
        viewPager.adapter = vpAdapter
        dot.attachTo(viewPager)

        courtName = findViewById(R.id.courtName)
        courtName.setText(courtDetail.Name)
        typeSportImage = findViewById(R.id.typeSportImage)
        val drawableID = resources.getIdentifier("${courtDetail.Type.lowercase()}_icon","drawable",packageName)
        typeSportImage.setImageDrawable(resources.getDrawable(drawableID))
        courtLocation = findViewById(R.id.courtLocation)
        courtLocation.setText(courtDetail.location!!.addressName)
        courtLocation.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        courtPhone =  findViewById(R.id.courtPhone)
        courtPhone.setText(courtDetail.Phone)
        courtPrice =  findViewById(R.id.courtPrice)
        courtPrice.setText(formatPrice(courtDetail.Price))
        courtLocation.setOnClickListener {
            val location =courtLocation.text.toString()
            val uri = Uri.parse("geo:0,0?q=$location")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        courtPhone.setOnClickListener {
            val phoneNumber =courtPhone.text.toString()
            val uri = Uri.parse("tel:$phoneNumber")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
    fun formatPrice(price: Int): String {
        val formatter = java.text.DecimalFormat("#,###")
        return formatter.format(price) + "đ / 60min"
    }
}