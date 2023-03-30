package com.example.sportbooking

import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class DetailCourtActivity : AppCompatActivity() {
    lateinit var viewPager:ViewPager2
    lateinit var dot:DotsIndicator

    lateinit var courtLocation:TextView
    lateinit var courtPhone:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_court)

        viewPager = findViewById(R.id.viewPager)
        dot = findViewById(R.id.dots_indicator)

        val drawable = resources.getDrawable(R.drawable.san_bong)
        val bitmap = (drawable as BitmapDrawable).bitmap

        val court = Court(1,1,"Sân bóng Trí Hải", "227 Đ.Nguyễn Văn Cừ, Phường 4, Quận 5, Hồ Chí Minh",
            "Bóng đá", "", ArrayList(), arrayListOf(bitmap,bitmap,bitmap, bitmap),ArrayList(),"",
            ArrayList(),50000 , 2.7,25,90,500.0
        )
        val vpAdapter = ImageViewPagerAdapter(arrayListOf(bitmap,bitmap,bitmap, bitmap))
        viewPager.setPageTransformer(MarginPageTransformer(37));
        viewPager.adapter = vpAdapter
        dot.attachTo(viewPager)


        courtLocation = findViewById(R.id.courtLocation)
        courtLocation.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        courtPhone =  findViewById(R.id.courtPhone)

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
}