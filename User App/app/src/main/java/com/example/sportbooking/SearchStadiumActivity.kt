package com.example.sportbooking

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity


class SearchStadiumActivity : AppCompatActivity() {
    private lateinit var typeOfSportInput: Spinner
    private lateinit var capacityInput: Spinner
    private lateinit var nameOfStadiumInput: EditText
    private lateinit var cityInput: Spinner
    private lateinit var districtInput: Spinner
    private lateinit var depositInput: SeekBar
    private lateinit var clearButton: Button
    private lateinit var searchButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_stadium)

        val typeOfSportList = arrayListOf(
            "Football", "Basketball", "Tennis", "Badminton", "Volleyball", "Table tennis", "Other"
        )
        val capacityList = arrayListOf(
            "1-10", "11-20", "21-30", "31-40", "41-50", "51-60", "61-70", "71-80", "81-90", "91-100"
        )
        val cityList = arrayListOf("Ho Chi Minh", "Binh Duong", "Dong Nai")
        val hoChiMinhDistricts = arrayListOf(
            "Quận 1", "Quận 2", "Quận 3", "Quận 4", "Quận 5", "Quận 6",
            "Quận 7", "Quận 8", "Quận 9", "Quận 10", "Quận 11", "Quận 12",
            "Bình Tân", "Bình Thạnh", "Gò Vấp", "Phú Nhuận", "Tân Bình",
            "Tân Phú", "Thủ Đức", "Bình Chánh", "Cần Giờ", "Củ Chi", "Hóc Môn",
            "Nhà Bè"
        )
        val binhDuongDistricts = arrayListOf(
            "Thủ Dầu Một", "Bàu Bàng", "Bắc Tân Uyên", "Bến Cát", "Dầu Tiếng",
            "Định Quán", "Phú Giáo", "Tân Uyên", "Tân Thành", "Dĩ An", "Thuận An"
        )
        val dongNaiDistricts = arrayListOf(
            "Biên Hòa", "Long Khánh", "Nhơn Trạch", "Long Thành", "Trảng Bom",
            "Xuân Lộc", "Cẩm Mỹ", "Thống Nhất", "Vĩnh Cửu", "Định Quán"
        )

        typeOfSportInput = findViewById(R.id.typeOfSportSpinner)
        capacityInput = findViewById(R.id.CapacitySpinner)
        nameOfStadiumInput = findViewById(R.id.nameOfStadEV)
        cityInput = findViewById(R.id.CitySpinner)
        districtInput = findViewById(R.id.districtSpinner)
        depositInput = findViewById(R.id.seekBar)
        clearButton = findViewById(R.id.clearButton)
        searchButton = findViewById(R.id.searchButton)

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
                when (position) {
                    0 -> {
                        ArrayAdapter(
                            this@SearchStadiumActivity,
                            android.R.layout.simple_spinner_item,
                            hoChiMinhDistricts
                        ).also { adapter ->
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            districtInput.adapter = adapter
                        }
                    }
                    1 -> {
                        ArrayAdapter(
                            this@SearchStadiumActivity,
                            android.R.layout.simple_spinner_item,
                            binhDuongDistricts
                        ).also { adapter ->
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            districtInput.adapter = adapter
                        }
                    }
                    2 -> {
                        ArrayAdapter(
                            this@SearchStadiumActivity,
                            android.R.layout.simple_spinner_item,
                            dongNaiDistricts
                        ).also { adapter ->
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            districtInput.adapter = adapter
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        depositInput.max = 20000000
        this.depositInput.progress = 100000

        depositInput.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            var progressChangedValue: Int = 0
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Log.d("seekbar", "onProgressChanged: $progress")
                progressChangedValue = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Log.d("seekbar", "onStartTrackingTouch: ")
                // TODO:
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Log.d("seekbar", "onStopTrackingTouch: ")
                Toast.makeText(
                    this@SearchStadiumActivity,
                    "Deposit: $progressChangedValue",
                    Toast.LENGTH_SHORT
                ).show()
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