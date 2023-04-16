package com.example.sportbooking_owner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shuhart.stepview.StepView
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.util.*


class NewFormatActivity : AppCompatActivity() {
    lateinit var stepViewLayout: View
    lateinit var stepView: StepView

    lateinit var typeSport: EditText
    lateinit var locationInput: EditText
    lateinit var priceInput: EditText
    lateinit var phoneInput: EditText
    lateinit var finalStepBtn: Button
    lateinit var newCourt: Courts
    lateinit var location: Location
    lateinit var yardPicker: com.shawnlin.numberpicker.NumberPicker
    companion object {
        val PICK_SPORT_TYPE_REQUEST = 200
        val PICK_LOCATION_REQUEST = 204
        val TIMELINE_STEP_REQUEST = 202
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_format)


        Places.initialize(this, "AIzaSyB352MaQT56jsnR1N4mDqPUEh3GPEhiRvE", Locale("vi", "VN"))

        stepViewLayout = findViewById(R.id.step_view_layout)
        stepView = stepViewLayout.findViewById(R.id.step_view)
        stepView.go(1, true)

        typeSport = findViewById(R.id.typeSportEdit)
        locationInput = findViewById(R.id.locationInput)
        locationInput.isFocusable = false
        phoneInput = findViewById(R.id.phoneInput)
        priceInput = findViewById(R.id.priceInput)

        locationInput.setOnClickListener {
            var fieldList = arrayListOf<Place.Field>(Place.Field.ADDRESS, Place.Field.LAT_LNG)
            val intent =
                Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                    .setCountries(listOf("VN"))
                    .build(this)
            startActivityForResult(intent, PICK_LOCATION_REQUEST)
        }
        typeSport.setOnClickListener {
            val intent = Intent(this, SelectSportActivity::class.java)
            startActivityForResult(intent, PICK_SPORT_TYPE_REQUEST)
        }
        yardPicker = findViewById(R.id.yardNumberPicker)
        finalStepBtn = findViewById(R.id.finalStepBtn)
        finalStepBtn.setOnClickListener {
            if (typeSport.text.toString().length == 0 || locationInput.text.toString().length == 0 || phoneInput.text.toString().length == 0 || priceInput.text.toString().length == 0) {
                MotionToast.createToast(this,
                    "Warning",
                    "Please fill all the details!",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            }else{
                val intent = Intent(this, NewTimelineActivity::class.java)
                startActivityForResult(intent, TIMELINE_STEP_REQUEST)
            }
        }
        findViewById<FloatingActionButton>(R.id.backBtn).setOnClickListener {
            val intent = intent
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_LOCATION_REQUEST && resultCode == RESULT_OK) {
            var place = Autocomplete.getPlaceFromIntent(data)
            locationInput.setText(place.address)
            var latLng= LatLng(place.latLng.latitude, place.latLng.longitude)
            location = Location(place.address, latLng)
        }
        if (requestCode == PICK_SPORT_TYPE_REQUEST && resultCode == RESULT_OK) {
            var typeSportStr = data!!.getStringExtra("type")
            typeSport.setText(typeSportStr)
        }
        if (requestCode == TIMELINE_STEP_REQUEST && resultCode == RESULT_OK) {
            newCourt = data!!.getParcelableExtra<Courts>("court")!!
            newCourt.Phone = phoneInput.text.toString()
            newCourt.location = location
            newCourt.Type = typeSport.text.toString()
            if(findViewById<CheckBox>(R.id.freeParkCb).isChecked)
                newCourt.AvalableService.add("Free parking")
            if(findViewById<CheckBox>(R.id.freeWifiCb).isChecked)
                newCourt.AvalableService.add("Free wifi")
            if(findViewById<CheckBox>(R.id.refereesCb).isChecked)
                newCourt.AvalableService.add("Referees available")
            if(findViewById<CheckBox>(R.id.shoesCb).isChecked)
                newCourt.AvalableService.add("Shoes for rent")
            newCourt.Price = priceInput.text.toString().toInt()
            newCourt.numOfYards = yardPicker.value
            val intent = intent
            intent.putExtra("court",newCourt)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

}