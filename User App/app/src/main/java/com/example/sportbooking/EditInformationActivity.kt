package com.example.sportbooking

import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.annotation.RequiresApi
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class EditInformationActivity : AppCompatActivity() {
    lateinit var changeImgBtn: ImageButton
    lateinit var fullNameInput: EditText
    lateinit var genderInput: Spinner
    lateinit var dobInput: EditText
    lateinit var phoneInput: EditText

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_information)

        val genderList = arrayListOf<String>("Male", "Female", "Other")
        changeImgBtn = findViewById(R.id.changeAvtBtn)
        fullNameInput = findViewById(R.id.fullnameInput)
        genderInput = findViewById(R.id.genderSpinner)
        dobInput = findViewById(R.id.dobInput)
        phoneInput = findViewById(R.id.phoneInput)

        ArrayAdapter(this, android.R.layout.simple_spinner_item, genderList)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                genderInput.adapter = adapter
            }
        dobInput.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .build()
            datePicker.show(supportFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener {
                val selectedDate = datePicker.selection
                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                var dob = Date(selectedDate!!)
                dobInput.setText(dateFormat.format(dob))
            }
        }

    }
}