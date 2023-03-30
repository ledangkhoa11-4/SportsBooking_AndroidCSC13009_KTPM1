package com.example.sportbooking

import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class DetailCourtActivity : AppCompatActivity() {
    lateinit var viewPager:ViewPager2
    lateinit var dot:DotsIndicator
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
    }
}