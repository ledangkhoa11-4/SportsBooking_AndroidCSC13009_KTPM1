package com.example.sportbooking

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoField

class CalendarViewActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)
        var calendar = findViewById<CalendarView>(R.id.simpleCalendarView)
        val now = LocalDateTime.now()
        val nowInMillis = (now.toEpochSecond(ZoneOffset.UTC) * 1000
                + now[ChronoField.MILLI_OF_SECOND])
        calendar.setDate(nowInMillis)
        calendar.setFirstDayOfWeek(2)
//        calendar.setDateTextAppearance("#ff33cc".toInt())
        calendar.setOnDateChangeListener(
            { view, year, month, dayOfMonth ->
                startActivityForResult(Intent(this,CourtScheduleActivity::class.java),111)
            })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode === 111){
            if(resultCode === 222){

            }
        }
    }
}