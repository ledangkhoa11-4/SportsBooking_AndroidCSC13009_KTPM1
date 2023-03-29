package com.example.sportbooking_owner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.FragmentManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.shuhart.stepview.StepView
import java.util.PropertyPermission

class NewTimelineActivity : AppCompatActivity() {
    lateinit var stepViewLayout: View
    lateinit var stepView: StepView

    lateinit var timeStart:EditText
    lateinit var timeEnd:EditText
    var hourStart:Int = 12
    var minuteStart:Int = 0
    var hourEnd:Int = 13
    var minuteEnd:Int = 0
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

        timeStart = findViewById(R.id.timeStart)

        timeEnd = findViewById(R.id.timeEnd)

        datePickerView = findViewById(R.id.daypicker)
        tM = datePickerView.findViewById(R.id.tM)
        tTue = datePickerView.findViewById(R.id.tTue)
        tW = datePickerView.findViewById(R.id.tW)
        tT = datePickerView.findViewById(R.id.tT)
        tF = datePickerView.findViewById(R.id.tF)
        tS = datePickerView.findViewById(R.id.tS)
        tSu = datePickerView.findViewById(R.id.tSu)

        val startPicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hourStart)
                .setMinute(minuteStart)
                .setTitleText("Select opening time")
                .build()
        val endPicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hourEnd)
                .setMinute(minuteEnd)
                .setTitleText("Select closing time")
                .build()
        startPicker.addOnPositiveButtonClickListener {
            var time = "";
            if(timeEnd.text.toString().length != 0){

                if((hourEnd+minuteEnd/60.0) - (startPicker.hour+startPicker.minute/60.0) <= 0){
                    Toast.makeText(this, "Opening time must earlier than Closing time",Toast.LENGTH_SHORT).show()
                    timeStart.setText("")
                }else
                    if((hourEnd+minuteEnd/60.0) - (startPicker.hour+startPicker.minute/60.0) < 2){
                        Toast.makeText(this, "Operating time must at least 2 hour",Toast.LENGTH_SHORT).show()
                        timeStart.setText("")
                    }else{
                        time = startPicker.hour.toString().padStart(2,'0') + ":" +startPicker.minute.toString().padStart(2,'0')
                        timeStart.setText(time)
                        hourStart = startPicker.hour
                        minuteStart = startPicker.minute
                    }

            }else{
                time = startPicker.hour.toString().padStart(2,'0') + ":" +startPicker.minute.toString().padStart(2,'0')
                timeStart.setText(time)
                hourStart = startPicker.hour
                minuteStart = startPicker.minute
            }
        }
        endPicker.addOnPositiveButtonClickListener {
            var time = "";
            if(timeStart.text.toString().length != 0){
                if((endPicker.hour+endPicker.minute/60.0) - (hourStart+minuteStart/60.0) <= 0){
                    Toast.makeText(this, "Opening time must earlier than Closing time",Toast.LENGTH_SHORT).show()
                    timeEnd.setText("")
                }else
                    if((endPicker.hour+endPicker.minute/60.0) - (hourStart+minuteStart/60.0) < 2){
                        Toast.makeText(this, "Operating time must at least 2 hour ",Toast.LENGTH_SHORT).show()
                        timeEnd.setText("")
                    }else{
                        time = endPicker.hour.toString().padStart(2,'0') + ":" +endPicker.minute.toString().padStart(2,'0')
                        timeEnd.setText(time)
                        hourEnd = endPicker.hour
                        minuteEnd = endPicker.minute
                    }
            }else{
                time = endPicker.hour.toString().padStart(2,'0') + ":" +endPicker.minute.toString().padStart(2,'0')
                timeEnd.setText(time)
                hourEnd = endPicker.hour
                minuteEnd = endPicker.minute
            }
        }

        timeStart.setOnClickListener {
            startPicker.show(supportFragmentManager, "tag");
        }
        timeEnd.setOnClickListener {
            endPicker.show(supportFragmentManager, "tag");
        }
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
        }
    }
}