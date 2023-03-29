package com.example.sportbooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.islandparadise14.mintable.MinTimeTableView
import com.islandparadise14.mintable.model.ScheduleEntity

class CourtScheduleActivity : AppCompatActivity() {
    private val day = arrayOf("Field1", "Field2", "Field3", "Field4", "Field5", "Field6")
    private val scheduleList: ArrayList<ScheduleEntity> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_court_schedule)
        scheduleList.add(
            ScheduleEntity(0,"incognito","2030-24",1,"20:30",
            "24:00","#00e600","#000000")
        )
        scheduleList.add(
            ScheduleEntity(1,"incognito","830-10",0,"8:30",
                "10:00","#73fcae68","#000000")
        )
        var table = findViewById<MinTimeTableView>(R.id.table)
        table.initTable(day)

        table.updateSchedules(scheduleList)
        findViewById<Button>(R.id.backBtn).setOnClickListener{
            finish()
        }
    }
}