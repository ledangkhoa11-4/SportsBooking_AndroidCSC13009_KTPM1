package com.example.sportbooking

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
    lateinit var sharedPref:SharedPreferences
    lateinit var provinces:Array<Province>
    var currentDistrict:Array<District>? = null
    lateinit var districtArrayAdapter: DistrictSpinnerAdapter
    var listCourt = kotlin.collections.ArrayList<Court>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_stadium)
        sharedPref = getSharedPreferences("filter", Context.MODE_PRIVATE)

        var lastType = sharedPref.getInt("type",0)
        var lastName = sharedPref.getString("name","")
        var priceFrom = sharedPref.getInt("priceFrom",-1)
        var priceTo = sharedPref.getInt("priceTo",-1)

        if(HomeActivity.lastCourList!=null){
            listCourt = HomeActivity.lastCourList!!
        }else{
            listCourt = HomeActivity.courtList_Home!!
        }

        typeOfSportInput = findViewById(R.id.typeOfSportSpinner)

        nameOfStadiumInput = findViewById(R.id.nameOfStadEV)
        cityInput = findViewById(R.id.CitySpinner)
        districtInput = findViewById(R.id.districtSpinner)
        depositInput = findViewById(R.id.seekBar)
        clearButton = findViewById(R.id.clearButton)
        searchButton = findViewById(R.id.searchButton)


        nameOfStadiumInput.setText(lastName)
        findViewById<ImageButton>(R.id.backButtonRating).setOnClickListener {
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
            "All", "Football", "Basketball", "Tennis", "Badminton", "Volleyball", "Table tennis"
        )

        ArrayAdapter(this, android.R.layout.simple_spinner_item, typeOfSportList).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            typeOfSportInput.adapter = adapter
        }
        typeOfSportInput.setSelection(lastType)
        cityInput.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val code = provinces[p2].code
                val task = NetworkTask(client, "https://provinces.open-api.vn/api/p/$code?depth=2")
                task.execute()
                val jsonProvince = task.get()

                val provinceTarget:Province = gson.fromJson(jsonProvince, Province::class.java)
                currentDistrict = provinceTarget.districts
                districtArrayAdapter = DistrictSpinnerAdapter(this@SearchStadiumActivity, provinceTarget.districts)
                districtInput.adapter = districtArrayAdapter

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        var values = ArrayList<Float>()
        if( listCourt.minByOrNull { it.Price } != null ){
            depositInput.valueFrom = listCourt.minByOrNull { it.Price }!!.Price.toFloat()
            values.add((depositInput.valueFrom))
        }else{
            depositInput.valueFrom = 0f
            values.add((depositInput.valueFrom))
        }
        if( listCourt.maxByOrNull { it.Price } != null ){
            depositInput.valueTo = listCourt.maxByOrNull { it.Price }!!.Price!!.toFloat()
            values.add((depositInput.valueTo))
        }else{
            depositInput.valueTo = 1f
            values.add((depositInput.valueTo))
        }
        if(priceFrom<=priceTo && priceFrom >= values[0] && priceTo <= values[1]){
            if(priceFrom!=-1){
                depositInput.valueFrom = priceFrom.toFloat()
            }
            if(priceTo!=-1){
                depositInput.valueTo = priceTo.toFloat()
            }
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
            if(HomeActivity.lastCourList!=null){
                HomeActivity.courtList_Home!!.clear()
                HomeActivity.courtList_Home!!.addAll(HomeActivity.lastCourList!!.toList())
            }
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()
        }

        searchButton.setOnClickListener {
            val typeOfSport = if(typeOfSportInput.selectedItem.toString() == "All") "" else typeOfSportInput.selectedItem.toString()
            val nameOfStadium = nameOfStadiumInput.text.toString()
            var city = provinces[cityInput.selectedItemPosition].name
            var district =  currentDistrict!![districtInput.selectedItemPosition].name
            city = city.replace("Tỉnh","").trim()
            city = city.replace("Thành phố","").trim()
            district = district.replace("Quận","").trim()
            district = district.replace("Huyện","").trim()
            district = district.replace("Thành phố","").trim()
            district = district.replace("Thị xã","").trim()

            val priceFrom = depositInput.valueFrom.toInt()
            val priceTo = depositInput.valueTo.toInt()
            if(HomeActivity.lastCourList == null){
                HomeActivity.lastCourList = java.util.ArrayList<Court>();
                HomeActivity.lastCourList!!.addAll(HomeActivity.courtList_Home!!.toList())
            }
            var searchedCourt = kotlin.collections.ArrayList<Court>()
            for(c in listCourt!!){
                if(c.Type.contains(typeOfSport,true) && c.Name.contains(nameOfStadium,true)
                    && c.location!!.addressName.contains(city,true)
                    && c.location!!.addressName.contains(district,true)
                    && c.Price >= priceFrom && c.Price <= priceTo)
                    searchedCourt.add(c)
            }
            HomeActivity.courtList_Home!!.clear()
            HomeActivity.courtList_Home!!.addAll(searchedCourt)
            HomeActivity.listViewAdapter!!.notifyDataSetChanged()
            Log.i(priceFrom.toString(), priceTo.toString())
            CreateToast.createToast(this,"${searchedCourt.size} results found!", "",true)
            val editor = sharedPref.edit()
            editor.putInt("type",typeOfSportInput.selectedItemPosition)
            editor.putString("name",nameOfStadium)
            editor.putInt("priceFrom",priceFrom)
            editor.putInt("priceTo",priceTo)
            editor.apply()
            finish()
        }
    }
    fun formatPrice(price: Int): String {
        val formatter = java.text.DecimalFormat("#,###")
        return formatter.format(price) + "đ"
    }
}