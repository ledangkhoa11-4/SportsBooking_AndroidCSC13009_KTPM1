package com.example.sportbooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hadi.emojiratingbar.EmojiRatingBar

class RatingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)
        findViewById<EmojiRatingBar>(R.id.emoji_rating_bar1).setReadOnly(true)
    }
}