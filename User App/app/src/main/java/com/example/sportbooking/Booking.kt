package com.example.sportbooking

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        val intent = intent
        val index = intent.getIntExtra("index",0)
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
            }

            override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {
                durationBooking = duration.durationMinutes;
            }

            override fun onEndTimeChange(endTime: TimeRangePicker.Time) {
                timeEnd.text = endTime.hour.toString().padStart(2, '0')+":"+endTime.minute.toString().padStart(2, '0')
            }

        })
        val formatter = java.text.DecimalFormat("#,###")
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
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilder.build())
                .setTitleText("Select date")
                .build()
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
        yardNum.setOnClickListener {
            if(numberPickerView.parent != null){
                var vg = numberPickerView.parent as ViewGroup
                vg.removeView(numberPickerView)
            }
            MaterialAlertDialogBuilder(this)
                .setTitle("Pick yard number")
                .setView(numberPickerView)
                .setPositiveButton("Apply") { dialog, which ->
                    yardNum.setText("Field " + numberPicker.value)
                }.show()
        }
        findViewById<ImageButton>(R.id.backButtonBooking).setOnClickListener {
            finish()
        }



//        dateButton = findViewById(R.id.pick_date_button)
//        dateSelected = findViewById(R.id.show_selected_date)
//        timeButton = findViewById(R.id.pick_time_button)
//        timeSelected = findViewById(R.id.show_selected_time)
//        checkoutButton = findViewById(R.id.checkout_button)
//
//        val materialDateBuilder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
//
//        materialDateBuilder.setTitleText("Select a date")
//        val materialDatePicker = materialDateBuilder.build()
//
//        dateButton.setOnClickListener {
//            materialDatePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
//        }
//
//        materialDatePicker.addOnPositiveButtonClickListener {
//            dateSelected.text = materialDatePicker.headerText
//        }
//
//        timeButton.setOnClickListener {
//            val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
//                .setTitleText("SELECT YOUR TIMING")
//
//                .setHour(12) // set the default hour for the dialog when the dialog opens
//
//                .setMinute(10) // set the default minute for the dialog when the dialog opens
//
//                .setTimeFormat(TimeFormat.CLOCK_12H) // set the time format according to the region
//                .build()
//
//            materialTimePicker.show(supportFragmentManager, "Booking")
//            materialTimePicker.addOnPositiveButtonClickListener {
//
//                val pickedHour: Int = materialTimePicker.hour
//                val pickedMinute: Int = materialTimePicker.minute
//
//                // check for single digit hour hour and minute and update TextView accordingly
//                val formattedTime: String = when {
//                    pickedHour > 12 -> {
//                        if (pickedMinute < 10) {
//                            "${materialTimePicker.hour - 12}:0${materialTimePicker.minute} pm"
//                        } else {
//                            "${materialTimePicker.hour - 12}:${materialTimePicker.minute} pm"
//                        }
//                    }
//                    pickedHour == 12 -> {
//                        if (pickedMinute < 10) {
//                            "${materialTimePicker.hour}:0${materialTimePicker.minute} pm"
//                        } else {
//                            "${materialTimePicker.hour}:${materialTimePicker.minute} pm"
//                        }
//                    }
//                    pickedHour == 0 -> {
//                        if (pickedMinute < 10) {
//                            "${materialTimePicker.hour + 12}:0${materialTimePicker.minute} am"
//                        } else {
//                            "${materialTimePicker.hour + 12}:${materialTimePicker.minute} am"
//                        }
//                    }
//                    else -> {
//                        if (pickedMinute < 10) {
//                            "${materialTimePicker.hour}:0${materialTimePicker.minute} am"
//                        } else {
//                            "${materialTimePicker.hour}:${materialTimePicker.minute} am"
//                        }
//                    }
//                }
//                timeSelected.text = formattedTime // update the preview TextView
//            }
//        }
//
//        checkoutButton.setOnClickListener {
//            val intent = Intent(this@Booking, Checkout::class.java)
//            startActivity(intent)
//        }
    }
}

class DateValidator(var ServiceWeekdays:String) : CalendarConstraints.DateValidator{
    private val today: Long = MaterialDatePicker.todayInUtcMilliseconds()
    constructor(parcel: Parcel) : this("") {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {}

    override fun isValid(date: Long): Boolean {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = date

        when(calendar.get(Calendar.DAY_OF_WEEK)){
            Calendar.MONDAY -> if(!ServiceWeekdays.contains("Mon",true)) return false
            Calendar.TUESDAY -> if(!ServiceWeekdays.contains("Tue",true)) return false
            Calendar.WEDNESDAY -> if(!ServiceWeekdays.contains("Wed",true)) return false
            Calendar.THURSDAY -> if(!ServiceWeekdays.contains("Thu",true)) return false
            Calendar.FRIDAY -> if(!ServiceWeekdays.contains("Fri",true)) return false
            Calendar.SATURDAY -> if(!ServiceWeekdays.contains("Sat",true)) return false
            Calendar.SUNDAY -> if(!ServiceWeekdays.contains("Sun",true)) return false
        }

        // Disable all dates before today
        if (date < today) {
            return false
        }
        // Allow all other dates
        return true
    }

    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<DateValidator> {
        override fun createFromParcel(parcel: Parcel): DateValidator {
            return DateValidator(parcel)
        }

        override fun newArray(size: Int): Array<DateValidator?> {
            return arrayOfNulls(size)
        }
    }
}