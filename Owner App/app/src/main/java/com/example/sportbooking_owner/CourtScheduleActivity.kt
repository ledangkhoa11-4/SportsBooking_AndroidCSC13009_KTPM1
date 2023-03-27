package com.example.sportbooking_owner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.islandparadise14.mintable.MinTimeTableView
import com.islandparadise14.mintable.model.ScheduleEntity

class CourtScheduleActivity : AppCompatActivity() {
    private val day = arrayOf("Field1", "Field2", "Field3", "Field4", "Field5", "Field6")
    private val scheduleList: ArrayList<ScheduleEntity> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_court_schedule)
        val schedule = ScheduleEntity(32,"Database","IT Building 301",2,"20:30",
            "24:00","#73fcae68","#000000")
        scheduleList.add(schedule)
        scheduleList.add(
            ScheduleEntity(5,"San A Sang","8-930",0,"8:30",
                "10:00","#73fcae68","#000000"))
        var table = findViewById<MinTimeTableView>(R.id.table)
        table.initTable(day)

        table.isFullWidth(true)
        table.updateSchedules(scheduleList)
    }
}