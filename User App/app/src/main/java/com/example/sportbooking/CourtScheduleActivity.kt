package com.example.sportbooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.islandparadise14.mintable.MinTimeTableView
import com.islandparadise14.mintable.model.ScheduleEntity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CourtScheduleActivity : AppCompatActivity() {
    lateinit var day:Array<String>
    private val scheduleList: ArrayList<ScheduleEntity> = ArrayList()
    lateinit var pickDateBtn:Button
    lateinit var court:Court
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_court_schedule)
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = today
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val startDate = today
        val endDate = calendar.apply {
            set(year, month, lastDayOfMonth)
        }.timeInMillis
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(startDate)
                .setEnd(endDate)
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pick a day to view schedule")
                .setCalendarConstraints(constraintsBuilder.build())
                .build()

        val intent = intent
        val index = intent.getIntExtra("index",0)
        court = HomeActivity.courtList_Home!![index]

        val timestart = convertTime(court.ServiceHour[0])
        val timeend = convertTime(court.ServiceHour[1])
        scheduleList.add(
            ScheduleEntity(0,"incognito","2030-24",1,"${timestart}",
            "${timestart}","#00e600","#000000")
        )
        scheduleList.add(
            ScheduleEntity(1,"incognito","830-10",0,"${timeend}",
                "${timeend}","#73fcae68","#000000")
        )

        pickDateBtn = findViewById<Button>(R.id.dateScheduleBtn)
        pickDateBtn.setText(convertDate(startDate))
        pickDateBtn.setOnClickListener {
            datePicker.show(supportFragmentManager, "tag");
        }
        datePicker.addOnPositiveButtonClickListener {
            var datePicked = convertDate(it);
            pickDateBtn.setText(datePicked)
        }
        day = Array(court.numOfYards) { i -> "F"+(i+1).toString() }

        var table = findViewById<MinTimeTableView>(R.id.table)
        table.initTable(day)

        table.updateSchedules(scheduleList)
        findViewById<Button>(R.id.backBtn).setOnClickListener{
            finish()
        }
    }
    fun convertTime(timeStamp:Long):String{
        val sdf = SimpleDateFormat("HH:mm") // create a SimpleDateFormat object with desired format
        val formattedTime = sdf.format(Date(timeStamp)) // convert timestamp to date and format as string
        return formattedTime
    }
    fun convertDate(timeStamp:Long):String{
        val sdf = SimpleDateFormat("dd/MM/yyyy") // create a SimpleDateFormat object with desired format
        val formattedTime = sdf.format(Date(timeStamp)) // convert timestamp to date and format as string
        return formattedTime
    }
}