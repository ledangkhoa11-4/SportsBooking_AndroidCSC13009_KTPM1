package com.example.sportbooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.sportbooking.DTO.BookingHistory
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.islandparadise14.mintable.MinTimeTableView
import com.islandparadise14.mintable.model.ScheduleEntity
import com.islandparadise14.mintable.tableinterface.OnTimeCellClickListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CourtScheduleActivity : AppCompatActivity() {
    lateinit var yards:Array<String>
    private val scheduleList: ArrayList<ScheduleEntity> = ArrayList()
    lateinit var pickDateBtn:Button
    lateinit var court:Court
    var bookList:kotlin.collections.ArrayList<BookingHistory> = ArrayList()
    lateinit var table: MinTimeTableView
    var selectedDate:Long = 0L
    companion object{

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_court_schedule)
        val intent = intent
        val index = intent.getIntExtra("index",0)
        selectedDate = System.currentTimeMillis()
        court = HomeActivity.courtList_Home!![index]
        table = findViewById<MinTimeTableView>(R.id.table)
        loadBookingList()
        val timestart = convertTime(court.ServiceHour[0])
        val timeend = convertTime(minusOneHourFromTimestamp(court.ServiceHour[1]))
        scheduleList.add(
            ScheduleEntity(0,"incognito","2030-24",0,"${timestart}",
                "${timestart}","#00e600","#000000")
        )
        scheduleList.add(
            ScheduleEntity(1,"incognito","830-10",0,"${timeend}",
                "${timeend}","#73fcae68","#000000")
        )
        yards = Array(court.numOfYards) { i -> "F"+(i+1).toString() }
        table.initTable(yards)
        updateScheduleByDate(selectedDate)

        val constraintsBuilder = CalendarConstraints.Builder()
        constraintsBuilder.setValidator(DateValidator(court.ServiceWeekdays))

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pick a day to view schedule")
                .setCalendarConstraints(constraintsBuilder.build())
                .setSelection(selectedDate)
                .build()

        pickDateBtn = findViewById(R.id.dateScheduleBtn)
        pickDateBtn.setText(convertDate(System.currentTimeMillis()))
        pickDateBtn.setOnClickListener {
            datePicker.show(supportFragmentManager, "tag");
        }
        datePicker.addOnPositiveButtonClickListener {
            var datePicked = convertDate(it);
            pickDateBtn.setText(datePicked)
            updateScheduleByDate(it);
            selectedDate = it
        }

        table.setOnTimeCellClickListener(object : OnTimeCellClickListener {
            override fun timeCellClicked(scheduleDay: Int, time: Int) {
                val yardNum = scheduleDay+1
                val hour = time
                val intent = Intent(applicationContext, Booking::class.java)
                intent.putExtra("index",index)
                intent.putExtra("date",datePicker.selection)
                intent.putExtra("yard",yardNum)
                intent.putExtra("hour",hour)
                startActivity(intent)
            }
        })

        findViewById<Button>(R.id.backBtn).setOnClickListener{
            finish()
        }
    }
    fun updateScheduleByDate(time:Long){
        scheduleList.subList(2,scheduleList.size).clear()
        for(book in bookList){
            if(convertDate(time) == convertDate(book.Date)){
                var startTime = convertTime(book.Time[0])
                var endTime = convertTime(book.Time[1])
                scheduleList.add(
                    ScheduleEntity(0,book.UserID,"${startTime} - ${endTime}",book.Yard-1,"${startTime}",
                        "${endTime}","#73fcae68","#000000")
                )
            }
        }
        table.updateSchedules(scheduleList)
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
    fun minusOneHourFromTimestamp(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.add(Calendar.HOUR_OF_DAY, -1)
        return calendar.timeInMillis
    }
    fun loadBookingList(){
        val bookingRef = MainActivity.database.getReference("Booking");
        val queryRef = bookingRef.orderByChild("CourtID").equalTo(court.CourtID)
        queryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookList.clear()
                for(ds in snapshot.children){
                    val bookHistory = ds.getValue(BookingHistory::class.java)
                    bookList.add(bookHistory!!)
                }
                updateScheduleByDate(selectedDate)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}