package com.example.sportbooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class Booking : AppCompatActivity() {
    private lateinit var dateButton: Button
    private lateinit var dateSelected: TextView
    private lateinit var timeButton: Button
    private lateinit var timeSelected: TextView
    private lateinit var checkoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        dateButton = findViewById(R.id.pick_date_button)
        dateSelected = findViewById(R.id.show_selected_date)
        timeButton = findViewById(R.id.pick_time_button)
        timeSelected = findViewById(R.id.show_selected_time)
        checkoutButton = findViewById(R.id.checkout_button)

        val materialDateBuilder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()

        materialDateBuilder.setTitleText("Select a date")
        val materialDatePicker = materialDateBuilder.build()

        dateButton.setOnClickListener {
            materialDatePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
        }

        materialDatePicker.addOnPositiveButtonClickListener {
            dateSelected.text = materialDatePicker.headerText
        }

        timeButton.setOnClickListener {
            val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
                .setTitleText("SELECT YOUR TIMING")

                .setHour(12) // set the default hour for the dialog when the dialog opens

                .setMinute(10) // set the default minute for the dialog when the dialog opens

                .setTimeFormat(TimeFormat.CLOCK_12H) // set the time format according to the region
                .build()

            materialTimePicker.show(supportFragmentManager, "Booking")
            materialTimePicker.addOnPositiveButtonClickListener {

                val pickedHour: Int = materialTimePicker.hour
                val pickedMinute: Int = materialTimePicker.minute

                // check for single digit hour hour and minute and update TextView accordingly
                val formattedTime: String = when {
                    pickedHour > 12 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour - 12}:0${materialTimePicker.minute} pm"
                        } else {
                            "${materialTimePicker.hour - 12}:${materialTimePicker.minute} pm"
                        }
                    }
                    pickedHour == 12 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour}:0${materialTimePicker.minute} pm"
                        } else {
                            "${materialTimePicker.hour}:${materialTimePicker.minute} pm"
                        }
                    }
                    pickedHour == 0 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour + 12}:0${materialTimePicker.minute} am"
                        } else {
                            "${materialTimePicker.hour + 12}:${materialTimePicker.minute} am"
                        }
                    }
                    else -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour}:0${materialTimePicker.minute} am"
                        } else {
                            "${materialTimePicker.hour}:${materialTimePicker.minute} am"
                        }
                    }
                }
                timeSelected.text = formattedTime // update the preview TextView
            }
        }

        checkoutButton.setOnClickListener {
            val intent = Intent(this@Booking, Checkout::class.java)
            startActivity(intent)
        }
    }
}