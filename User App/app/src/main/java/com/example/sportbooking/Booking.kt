package com.example.sportbooking

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shawnlin.numberpicker.NumberPicker
import nl.joery.timerangepicker.TimeRangePicker
import java.text.SimpleDateFormat
import java.util.*


class Booking : AppCompatActivity() {
    lateinit var courtNameTv:TextView
    lateinit var courtLocation:TextView
    lateinit var sportType:TextView
    lateinit var yardNum:TextView
    lateinit var dateTv:TextView
    lateinit var timeTv:TextView
    lateinit var court:Court
    lateinit var timeRangePicker:TimeRangePicker
    lateinit var timeStart:TextView
    lateinit var timeEnd:TextView
    lateinit var priceBooking:TextView
    var durationBooking:Int = 0;
    var index = -1
    var date = -1L
    var yard = -1
    var start = -1L
    var end = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        val formatter = java.text.DecimalFormat("#,###")
        val intent = intent
        index = intent.getIntExtra("index",0)
        date = intent.getLongExtra("date",-1)
        yard = intent.getIntExtra("yard",-1)
        var hour = intent.getIntExtra("hour",-1)
        court = MainActivity.listCourt[index]
        courtNameTv = findViewById(R.id.courtNameBookingTv)
        courtLocation = findViewById(R.id.courtLocation)
        sportType = findViewById(R.id.typeSport)
        yardNum = findViewById(R.id.yardNumChoose)
        dateTv = findViewById(R.id.dateChoose)
        timeTv = findViewById(R.id.timeChoose)
        priceBooking = findViewById(R.id.priceBooking)

        courtNameTv.setText(court.Name)
        courtLocation.setText(court.location!!.addressName)
        sportType.setText(court.Type)


        val pickTimeView:View = LayoutInflater.from(this).inflate(R.layout.time_picker_layout,null);
        timeRangePicker = pickTimeView.findViewById(R.id.timeRangePicker)
        timeRangePicker.startTime = TimeRangePicker.Time(0,0)
        timeStart = pickTimeView.findViewById(R.id.startTimeTv)
        timeEnd = pickTimeView.findViewById(R.id.endTimeTv)
        timeRangePicker.setOnTimeChangeListener(object : TimeRangePicker.OnTimeChangeListener {
            override fun onStartTimeChange(startTime: TimeRangePicker.Time) {
                timeStart.text = startTime.hour.toString().padStart(2, '0')+":"+startTime.minute.toString().padStart(2, '0')
                start = timeStringToTimestamp(timeStart.text.toString())
            }

            override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {
                durationBooking = duration.durationMinutes;
            }

            override fun onEndTimeChange(endTime: TimeRangePicker.Time) {
                timeEnd.text = endTime.hour.toString().padStart(2, '0')+":"+endTime.minute.toString().padStart(2, '0')
                end = timeStringToTimestamp(timeEnd.text.toString())
            }

        })
        if(hour != -1){
            timeStart.text = hour.toString().padStart(2, '0')+":"+0.toString().padStart(2, '0')
            timeEnd.text = (hour+1).toString().padStart(2, '0')+":"+0.toString().padStart(2, '0')
            timeRangePicker.startTimeMinutes = hour*60
            timeRangePicker.endTimeMinutes = (hour+1)*60
            timeTv.setText(timeStart.text.toString() + " - " + timeEnd.text.toString())
            priceBooking.setText(formatter.format(court.Price)  + "đ")
        }

        timeTv.setOnClickListener {
            if(pickTimeView.parent != null){
                var vg = pickTimeView.parent as ViewGroup
                vg.removeView(pickTimeView)
            }
            MaterialAlertDialogBuilder(this)
                .setTitle("Pick booking time")
                .setView(pickTimeView)
                .setPositiveButton("Apply") { dialog, which ->
                    timeTv.setText(timeStart.text.toString() + " - " + timeEnd.text.toString())
                    priceBooking.setText(formatter.format((court.Price * durationBooking)/60) + "đ")
                }.show()
        }

        val today = Calendar.getInstance()
        val constraintsBuilder = CalendarConstraints.Builder()
        constraintsBuilder.setStart(today.timeInMillis)
        constraintsBuilder.setValidator(DateValidator(court.ServiceWeekdays))
        val datePickerBuilder =
            MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilder.build())
                .setTitleText("Select date")

        val datePicker:MaterialDatePicker<Long>
        if(date != -1L){
            datePicker = datePickerBuilder.setSelection(date).build()
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateTv.setText(sdf.format(Date(date)))
        }else{
            datePicker = datePickerBuilder.build()
        }
        dateTv.setOnClickListener {
            datePicker.show(supportFragmentManager, "tag");
        }
        datePicker.addOnPositiveButtonClickListener {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateTv.setText(sdf.format(Date(it)))
        }

        val numberPickerView:View = LayoutInflater.from(this).inflate(R.layout.yard_picker,null);
        val numberPicker:NumberPicker =numberPickerView.findViewById(R.id.yardNumberPicker)
        numberPicker.minValue = 1
        numberPicker.maxValue = court.numOfYards
        val pickYardDialog = MaterialAlertDialogBuilder(this)
            .setTitle("Pick yard number")
            .setView(numberPickerView)
            .setPositiveButton("Apply") { dialog, which ->
                yardNum.setText("Field " + numberPicker.value)
            }
        yardNum.setOnClickListener {
            if(numberPickerView.parent != null){
                var vg = numberPickerView.parent as ViewGroup
                vg.removeView(numberPickerView)
            }
            pickYardDialog.show()
        }
        if(yard != -1){
            yardNum.setText("Field " + yard)
        }
        findViewById<ImageButton>(R.id.backButtonBooking).setOnClickListener {
            finish()
        }
        findViewById<Button>(R.id.BookBtn).setOnClickListener {
            Log.i("AAAAAAAA",start.toString())
            Log.i("AAAAAAAA",end.toString())
        }

    }
    fun timeStringToTimestamp(timeString: String): Long {
        val format = SimpleDateFormat("hh:mm", Locale.getDefault())
        val date = format.parse(timeString)
        return date?.time ?: 0
    }
}

