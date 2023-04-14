package com.example.sportbooking

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.sportbooking.DTO.District
import com.example.sportbooking.DTO.Province
import com.google.android.material.slider.RangeSlider
import com.google.gson.Gson
import okhttp3.OkHttpClient
import java.util.*
import kotlin.collections.ArrayList


class SearchStadiumActivity : AppCompatActivity() {
    private lateinit var typeOfSportInput: Spinner

    private lateinit var nameOfStadiumInput: EditText
    private lateinit var cityInput: Spinner
    private lateinit var districtInput: Spinner
    private lateinit var depositInput: RangeSlider
    private lateinit var clearButton: Button
    private lateinit var searchButton: Button

    lateinit var provinces:Array<Province>
    lateinit var district:Array<District>
    lateinit var districtArrayAdapter: DistrictSpinnerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_stadium)
        typeOfSportInput = findViewById(R.id.typeOfSportSpinner)

        nameOfStadiumInput = findViewById(R.id.nameOfStadEV)
        cityInput = findViewById(R.id.CitySpinner)
        districtInput = findViewById(R.id.districtSpinner)
        depositInput = findViewById(R.id.seekBar)
        clearButton = findViewById(R.id.clearButton)
        searchButton = findViewById(R.id.searchButton)

        findViewById<ImageButton>(R.id.backButtonBookingDetail).setOnClickListener {
            finish()
        }
        val gson = Gson()
        val client = OkHttpClient()
        val url = "https://provinces.open-api.vn/api"
        val task = NetworkTask(client, url + "/p")
        task.execute()
        val jsonProvince = task.get()
        val tmp = gson.fromJson(jsonProvince, Array<Province>::class.java)
        if(tmp != null){
            provinces = tmp
        }else
            provinces = emptyArray()

        val provinceAdapter = ProvinceSpinnerAdapter(this, provinces)
        cityInput.adapter = provinceAdapter




        val typeOfSportList = arrayListOf(
            "Football", "Basketball", "Tennis", "Badminton", "Volleyball", "Table tennis", "Other"
        )

        ArrayAdapter(this, android.R.layout.simple_spinner_item, typeOfSportList).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            typeOfSportInput.adapter = adapter
        }
        cityInput.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val code = provinces[p2].code
                val task = NetworkTask(client, "https://provinces.open-api.vn/api/p/$code?depth=2")
                task.execute()
                val jsonProvince = task.get()

                val provinceTarget:Province = gson.fromJson(jsonProvince, Province::class.java)
                districtArrayAdapter = DistrictSpinnerAdapter(this@SearchStadiumActivity, provinceTarget.districts)
                districtInput.adapter = districtArrayAdapter

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

//        ArrayAdapter(this, android.R.layout.simple_spinner_item, capacityList).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            capacityInput.adapter = adapter
//        }
//
//        ArrayAdapter(this, android.R.layout.simple_spinner_item, cityList).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            cityInput.adapter = adapter
//        }
//
//        cityInput.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View,
//                position: Int,
//                id: Long
//            ) {
//                when (position) {
//                    0 -> {
//                        ArrayAdapter(
//                            this@SearchStadiumActivity,
//                            android.R.layout.simple_spinner_item,
//                            hoChiMinhDistricts
//                        ).also { adapter ->
//                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                            districtInput.adapter = adapter
//                        }
//                    }
//                    1 -> {
//                        ArrayAdapter(
//                            this@SearchStadiumActivity,
//                            android.R.layout.simple_spinner_item,
//                            binhDuongDistricts
//                        ).also { adapter ->
//                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                            districtInput.adapter = adapter
//                        }
//                    }
//                    2 -> {
//                        ArrayAdapter(
//                            this@SearchStadiumActivity,
//                            android.R.layout.simple_spinner_item,
//                            dongNaiDistricts
//                        ).also { adapter ->
//                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                            districtInput.adapter = adapter
//                        }
//                    }
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // Another interface callback
//            }
//        }
        var values = ArrayList<Float>()
        if( MainActivity.listCourt.minByOrNull { it.Price } != null ){
            depositInput.valueFrom = MainActivity.listCourt.minByOrNull { it.Price }!!.Price.toFloat()
            values.add((depositInput.valueFrom))
        }else{
            depositInput.valueFrom = 0f
            values.add((depositInput.valueFrom))
        }
        if( MainActivity.listCourt.maxByOrNull { it.Price } != null ){
            depositInput.valueTo = MainActivity.listCourt.maxByOrNull { it.Price }!!.Price!!.toFloat()
            values.add((depositInput.valueFrom))
        }else{
            depositInput.valueTo = 1f
            values.add((depositInput.valueFrom))
        }
        depositInput.values = values


        depositInput.setLabelFormatter { value: Float ->
            val intVal = value.toInt()
            formatPrice(intVal)
        }

        clearButton.setOnClickListener {
            typeOfSportInput.setSelection(0)

            nameOfStadiumInput.setText("")
            cityInput.setSelection(0)
            districtInput.setSelection(0)
        }

        searchButton.setOnClickListener {
            val typeOfSport = typeOfSportInput.selectedItem.toString()
            val nameOfStadium = nameOfStadiumInput.text.toString()
            val city = cityInput.selectedItem.toString()
            val district = districtInput.selectedItem.toString()

            Toast.makeText(
                this,
                "Type of sport: $typeOfSport \nName of stadium: $nameOfStadium \nCity: $city \nDistrict: $district",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    fun formatPrice(price: Int): String {
        val formatter = java.text.DecimalFormat("#,###")
        return formatter.format(price) + "đ"
    }
}