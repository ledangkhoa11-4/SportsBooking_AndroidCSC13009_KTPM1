package com.example.sportbooking_owner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.example.sportbooking_owner.DTO.Courts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shuhart.stepview.StepView
import nl.joery.timerangepicker.TimeRangePicker
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.text.SimpleDateFormat
import java.util.*

class NewTimelineActivity : AppCompatActivity() {
    lateinit var stepViewLayout: View
    lateinit var stepView: StepView

    lateinit var timeRangePicker:TimeRangePicker

    lateinit var timeStart:TextView
    lateinit var timeEnd:TextView

    lateinit var datePickerView: View
    lateinit var tM:ToggleButton
    lateinit var tTue:ToggleButton
    lateinit var tW:ToggleButton
    lateinit var tT:ToggleButton
    lateinit var tF:ToggleButton
    lateinit var tS:ToggleButton
    lateinit var tSu:ToggleButton
    lateinit var weekdaysChoice:String

    companion object {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_timeline)

        stepViewLayout = findViewById(R.id.step_view_layout)
        stepView = stepViewLayout.findViewById(R.id.step_view)
        stepView.go(2, true)
        overridePendingTransition(0, 0)



        datePickerView = findViewById(R.id.daypicker)
        tM = datePickerView.findViewById(R.id.tM)
        tTue = datePickerView.findViewById(R.id.tTue)
        tW = datePickerView.findViewById(R.id.tW)
        tT = datePickerView.findViewById(R.id.tT)
        tF = datePickerView.findViewById(R.id.tF)
        tS = datePickerView.findViewById(R.id.tS)
        tSu = datePickerView.findViewById(R.id.tSu)

        timeRangePicker = findViewById(R.id.timeRangePicker)
        timeRangePicker.startTime = TimeRangePicker.Time(0,0)


        timeStart = findViewById(R.id.startTimeTv)
        timeEnd = findViewById(R.id.endTimeTv)
        timeRangePicker.setOnTimeChangeListener(object : TimeRangePicker.OnTimeChangeListener {
            override fun onStartTimeChange(startTime: TimeRangePicker.Time) {
                timeStart.text = startTime.hour.toString().padStart(2, '0')+":"+startTime.minute.toString().padStart(2, '0')
            }

            override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {
                //ignore
            }

            override fun onEndTimeChange(endTime: TimeRangePicker.Time) {
                timeEnd.text = endTime.hour.toString().padStart(2, '0')+":"+endTime.minute.toString().padStart(2, '0')
            }

        })

        findViewById<FloatingActionButton>(R.id.backBtn).setOnClickListener {
            val intent = intent
            setResult(RESULT_CANCELED,intent)
            finish()
        }
        findViewById<Button>(R.id.finishBtn).setOnClickListener {
            weekdaysChoice = ""
            if(tM.isChecked()){
                weekdaysChoice +="Mon,";
            }
            if(tTue.isChecked()){
                weekdaysChoice +="Tue,";
            }
            if(tW.isChecked()){
                weekdaysChoice +="Wed,";
            }
            if(tT.isChecked()){
                weekdaysChoice +="Thu,";
            }
            if(tF.isChecked()){
                weekdaysChoice +="Fri,";
            }
            if(tS.isChecked()){
                weekdaysChoice +="Sat,";
            }
            if(tSu.isChecked()){
                weekdaysChoice +="Sun";
            }


            if(weekdaysChoice.length == 0 || timeRangePicker.duration.hour<2){
                var msg :String = ""
                if(weekdaysChoice.length == 0)
                    msg = "Please fill all the details!"
                else
                    msg = "Duration must be longer than 2 hours"
                MotionToast.createColorToast(this,
                    "Warning",
                    msg,
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            }else{
                var calendar = Calendar.getInstance()
                val id = calendar.timeInMillis
                var newCourt = Courts()
                newCourt.CourtID = id.toString()
                newCourt.ServiceWeekdays = weekdaysChoice
                val startTimeStamp = convertToTimestamp(timeStart.text.toString())
                val endTimeStamp = convertToTimestamp(timeEnd.text.toString())
                newCourt.ServiceHour.add(startTimeStamp)
                newCourt.ServiceHour.add(endTimeStamp)
                val intent = intent
                intent.putExtra("court",newCourt)
                setResult(RESULT_OK,intent)
                finish()
            }
        }
    }
    fun convertToTimestamp(time: String): Long {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        try{
            val date = sdf.parse(time)
            return date?.time ?: 0
        }catch (e:Exception){
            return 0
        }
    }
}