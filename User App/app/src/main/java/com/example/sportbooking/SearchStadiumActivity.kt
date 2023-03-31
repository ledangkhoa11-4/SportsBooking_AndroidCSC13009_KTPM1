package com.example.sportbooking

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity


class SearchStadiumActivity : AppCompatActivity() {
    lateinit var typeOfSportInput: Spinner
    lateinit var capacityInput: Spinner
    lateinit var nameOfStadiumInput: EditText
    lateinit var cityInput: Spinner
    lateinit var districtInput: Spinner
    lateinit var depositInput: SeekBar
    lateinit var clearButton: Button
    lateinit var searchButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_stadium)

        val typeOfSportList = arrayListOf<String>(
            "Football",
            "Basketball",
            "Tennis",
            "Badminton",
            "Volleyball",
            "Table tennis",
            "Other"
        )
        val capacityList = arrayListOf<String>(
            "1-10",
            "11-20",
            "21-30",
            "31-40",
            "41-50",
            "51-60",
            "61-70",
            "71-80",
            "81-90",
            "91-100",
            "101-110",
            "111-120",
            "121-130",
            "131-140",
            "141-150",
            "151-160",
            "161-170",
            "171-180",
            "181-190",
            "191-200"
        )
        val cityList = arrayListOf<String>("Ho Chi Minh", "Binh Duong", "Dong Nai")
        val hcm_districts = arrayListOf<String>(
            "Quận 1", "Quận 2", "Quận 3", "Quận 4", "Quận 5", "Quận 6",
            "Quận 7", "Quận 8", "Quận 9", "Quận 10", "Quận 11", "Quận 12",
            "Bình Tân", "Bình Thạnh", "Gò Vấp", "Phú Nhuận", "Tân Bình",
            "Tân Phú", "Thủ Đức", "Bình Chánh", "Cần Giờ", "Củ Chi", "Hóc Môn",
            "Nhà Bè"
        )
        val binhduong_districts = arrayListOf<String>(
            "Thành phố Thủ Dầu Một", "Huyện Bàu Bàng", "Huyện Bắc Tân Uyên",
            "Huyện Bến Cát", "Huyện Dầu Tiếng", "Huyện Định Quán",
            "Huyện Phú Giáo", "Huyện Tân Uyên", "Huyện Tân Thành",
            "Thị xã Bến Cát", "Thành phố Dĩ An", "Thị xã Thuận An"
        )


        typeOfSportInput = findViewById(R.id.typeOfSportSpinner)
        capacityInput = findViewById(R.id.CapacitySpinner)
        nameOfStadiumInput = findViewById(R.id.nameOfStadEV)
        cityInput = findViewById(R.id.CitySpinner)
        districtInput = findViewById(R.id.districtSpinner)
        depositInput = findViewById(R.id.seekBar)
        clearButton = findViewById(R.id.clearButton)
        searchButton = findViewById(R.id.searchButton)

        this.depositInput.max = 10000000
        this.depositInput.setProgress(100000)

        ArrayAdapter(this, android.R.layout.simple_spinner_item, typeOfSportList).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            typeOfSportInput.adapter = adapter
        }

        ArrayAdapter(this, android.R.layout.simple_spinner_item, capacityList).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            capacityInput.adapter = adapter
        }

        ArrayAdapter(this, android.R.layout.simple_spinner_item, cityList).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            cityInput.adapter = adapter
        }

        cityInput.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    ArrayAdapter(
                        this@SearchStadiumActivity,
                        android.R.layout.simple_spinner_item,
                        hcm_districts
                    ).also { adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        districtInput.adapter = adapter
                    }
                } else if (position == 1) {
                    ArrayAdapter(
                        this@SearchStadiumActivity,
                        android.R.layout.simple_spinner_item,
                        binhduong_districts
                    ).also { adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        districtInput.adapter = adapter
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        depositInput.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Log.d("seekbar", "onProgressChanged: $progress")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Log.d("seekbar", "onStartTrackingTouch: ")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Log.d("seekbar", "onStopTrackingTouch: ")
            }
        })

        clearButton.setOnClickListener {
            typeOfSportInput.setSelection(0)
            capacityInput.setSelection(0)
            nameOfStadiumInput.setText("")
            cityInput.setSelection(0)
            districtInput.setSelection(0)
            depositInput.progress = 100000
        }

        searchButton.setOnClickListener {
            val typeOfSport = typeOfSportInput.selectedItem.toString()
            val capacity = capacityInput.selectedItem.toString()
            val nameOfStadium = nameOfStadiumInput.text.toString()
            val city = cityInput.selectedItem.toString()
            val district = districtInput.selectedItem.toString()
            val deposit = depositInput.progress
            Toast.makeText(
                this,
                "Type of sport: $typeOfSport \nCapacity: $capacity \nName of stadium: $nameOfStadium \nCity: $city \nDistrict: $district \nDeposit: $deposit",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}