package com.example.sportbooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import com.hadi.emojiratingbar.EmojiRatingBar

class RatingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

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
    }

    fun loadRatingList(){
        val ratingRef = MainActivity.database.getReference("Rating");

    }
}