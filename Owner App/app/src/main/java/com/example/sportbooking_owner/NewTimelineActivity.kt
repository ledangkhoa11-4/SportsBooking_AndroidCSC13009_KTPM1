package com.example.sportbooking_owner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.FragmentManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.shuhart.stepview.StepView

class NewTimelineActivity : AppCompatActivity() {
    lateinit var stepViewLayout: View
    lateinit var stepView: StepView

    lateinit var timeStart:EditText
    lateinit var timeEnd:EditText

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

        val startPicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Appointment time")
                .build()
        val endPicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Appointment time")
                .build()
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
    }
}