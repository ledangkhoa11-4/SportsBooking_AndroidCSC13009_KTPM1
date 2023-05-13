package com.example.sportbooking_owner

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.sportbooking_owner.DTO.BookingHistory
import com.example.sportbooking_owner.DTO.Courts
import com.example.sportbooking_owner.DTO.User
import com.example.sportbooking_owner.Interfaces.CombinedEventListener
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.islandparadise14.mintable_khoa.MinTimeTableView_KhoaCustom
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CourtScheduleActivity : AppCompatActivity() {
    lateinit var tableMain: MinTimeTableView_KhoaCustom
    lateinit var tableV: MinTimeTableView_KhoaCustom
    lateinit var tableH: MinTimeTableView_KhoaCustom
    private val scheduleList: ArrayList<com.islandparadise14.mintable_khoa.model.ScheduleEntity> = ArrayList()
    lateinit var scroll_H: HorizontalScrollView
    lateinit var scroll_V: ScrollView
    lateinit var paramH: ViewGroup.MarginLayoutParams
    lateinit var paramV: ViewGroup.MarginLayoutParams
    lateinit var loadingScreen : ConstraintLayout
    var v_Start = 0
    var v_Top = 0
    var h_Start = 0
    var h_Top = 0
    var lastX = 0
    var lastY = 0
    var selectedDate:Long = 0L
    var bookList: ArrayList<BookingHistory> = ArrayList()
    lateinit var pickDateBtn: Button
    var yards = arrayOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_court_schedule)
        loadingScreen = findViewById(R.id.loadingScheduleIndicator)
        tableMain = findViewById(R.id.tableMain)
        tableV = findViewById(R.id.table_V)
        tableH = findViewById(R.id.table_H)
        scroll_H = findViewById(R.id.horizontalScroolView)
        scroll_V = findViewById(R.id.verticalScrollView)
        scroll_H.elevation = 9f
        scroll_V.elevation = 8f
        tableV.elevation = 1f
        tableH.elevation = 2f

        val timestart = "0:00"
        val timeend = "24:00"
        scheduleList.add(
            com.islandparadise14.mintable_khoa.model.ScheduleEntity(0,"incognito","2030-24",0,"${timestart}",
                "${timestart}","#00e600","#000000")
        )
        scheduleList.add(
            com.islandparadise14.mintable_khoa.model.ScheduleEntity(1,"incognito","830-10",0,"${timeend}",
                "${timeend}","#73fcae68","#000000")
        )
        val days:ArrayList<String> = arrayListOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun")
        var serviceDays:String = ""
        for(i in 0 until SignIn.listCourt.size){
            for(day in days){
                if(day in SignIn.listCourt[i].ServiceWeekdays && day !in serviceDays) serviceDays += day + ","
            }
        }
        serviceDays = serviceDays.substring(0,serviceDays.length - 1)
        selectedDate = System.currentTimeMillis()
        while(!getDayOfWeek(selectedDate,serviceDays)){
            selectedDate += (1000 * 60 * 60 * 24)
        }

        val constraintsBuilder = CalendarConstraints.Builder()
        constraintsBuilder.setValidator(DateValidator(serviceDays))

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pick a day to view schedule")
                .setCalendarConstraints(constraintsBuilder.build())
                .setSelection(selectedDate)
                .build()

        pickDateBtn = findViewById(R.id.dateScheduleBtn)

        pickDateBtn.setText("Pick a date")
        pickDateBtn.setOnClickListener {
            datePicker.show(supportFragmentManager, "tag");
        }
        datePicker.addOnPositiveButtonClickListener {
            var datePicked = convertDate(it);
            pickDateBtn.setText(datePicked)
            updateScheduleByDate(it);
            selectedDate = it
        }
        findViewById<Button>(R.id.backBtn).setOnClickListener{
            finish()
        }

        paramH = tableH.layoutParams as ViewGroup.MarginLayoutParams
        paramV = tableV.layoutParams as ViewGroup.MarginLayoutParams
        v_Start = paramV.marginStart
        v_Top = paramV.topMargin
        h_Start = paramH.marginStart
        h_Top = paramH.topMargin
        scroll_H.viewTreeObserver.addOnScrollChangedListener {
            val scroolX = scroll_H.scrollX
            if (lastX == scroolX)
                return@addOnScrollChangedListener
            lastX = scroolX

            paramH.topMargin = h_Top
            paramH.marginStart = h_Start - scroolX
            tableH.layoutParams = paramH

            tableH.elevation = 1f
            tableV.elevation = 2f

        }

        scroll_V.viewTreeObserver.addOnScrollChangedListener {
            val scroolY = scroll_V.scrollY
            if (lastY == scroolY)
                return@addOnScrollChangedListener
            lastY = scroolY

            paramV.topMargin = v_Top - scroolY
            paramV.marginStart = v_Start
            tableV.layoutParams = paramV

            tableH.elevation = 2f
            tableV.elevation = 1f
        }
        loadBookingList()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(yards.isEmpty()) {
            for (i in 0 until SignIn.listCourt.size) {
                val array = yards!!.toMutableList()
                val yard =
                    Array(SignIn.listCourt[i].numOfYards) { j -> SignIn.listCourt[i].Name + " " + (j + 1).toString() }
                array.addAll(yard)
                yards = array.toTypedArray()
            }
            tableMain.initTable(yards, 3)
            tableV.initTable(yards, 2)
            tableH.initTable(yards, 1)
        }

//        tableMain.updateSchedules(scheduleList)
//        tableV.updateSchedules(scheduleList)
//        tableH.updateSchedules(scheduleList)
    }

    fun updateScheduleByDate(time:Long){
        scheduleList.subList(2,scheduleList.size).clear()
        for(book in bookList){
            if(convertDate(time) == convertDate(book.Date)){
                var startTime = convertTime(book.Time[0])
                var endTime = convertTime(book.Time[1])
                var idx = 0
                for(i in 0 until SignIn.listCourt.size){
                    if(book.CourtID.equals(SignIn.listCourt[i].CourtID)){
                        idx += book.Yard
                        break
                    }
                    else idx += SignIn.listCourt[i].numOfYards

                }
                if(idx != 0) {
                    scheduleList.add(
                        com.islandparadise14.mintable_khoa.model.ScheduleEntity(
                            0, book.CustomerName, "${startTime} - ${endTime}", idx - 1, "${startTime}",
                            "${endTime}", "#73fcae68", "#000000"
                        )
                    )
                }
            }
        }
        tableMain.updateSchedules(scheduleList)
        tableV.updateSchedules(scheduleList)
        tableH.updateSchedules(scheduleList)
    }

    fun convertTime(timeStamp:Long):String{
        val sdf = SimpleDateFormat("HH:mm")
        val formattedTime = sdf.format(Date(timeStamp))
        return formattedTime
    }
    fun convertDate(timeStamp:Long):String{
        val sdf = SimpleDateFormat("dd/MM/yyyy") // create a SimpleDateFormat object with desired format
        val formattedTime = sdf.format(Date(timeStamp)) // convert timestamp to date and format as string
        return formattedTime
    }
    fun loadBookingList(){
        val userRef = MainActivity.database.getReference("User")
        loadingScreen.visibility = View.VISIBLE
        val combinedEventListener = object : CombinedEventListener{
            override fun onCombinedDataChanged(dataSnapshot: DataSnapshot) {
                Log.i("loadBookingList","getIn")
                for (ds in dataSnapshot.children) {
                    val bookHistory = ds.getValue(BookingHistory::class.java)
                    var userid = bookHistory!!.UserID
                    val userQuery = userRef.orderByChild("id").equalTo(userid)
                    userQuery.addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for(userDs in snapshot.children){
                                val user = userDs.getValue(User::class.java)
                                bookHistory.CustomerName = user!!.username
                            }
                            bookList.add(bookHistory!!)
                            updateScheduleByDate(selectedDate)
                            loadingScreen.visibility = View.GONE

                        }
                        override fun onCancelled(error: DatabaseError) {

                        }
                    })

                }
            }

            override fun onCombinedCancelled(databaseError: DatabaseError) {
                Log.i("Error", databaseError.message)
            }
        }

        val bookingRef = MainActivity.database.getReference("Booking");

        for(court in SignIn.listCourt){
            val queryRef = bookingRef.orderByChild("courtID").equalTo(court.CourtID)
            queryRef.addValueEventListener(combinedEventListener)
        }
    }

    fun getDayOfWeek(time:Long,ServiceWeekdays:String):Boolean{
        var calendar:Calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val format = SimpleDateFormat("EE").format(Date(time))
        if(format !in ServiceWeekdays) return false
        return true
    }
}