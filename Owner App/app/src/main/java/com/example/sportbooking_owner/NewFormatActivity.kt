package com.example.sportbooking_owner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.shuhart.stepview.StepView
import java.util.*


class NewFormatActivity : AppCompatActivity() {
    lateinit var stepViewLayout: View
    lateinit var stepView: StepView

    lateinit var typeSport:EditText
    lateinit var locationInput:EditText
    companion object {
        val PICK_SPORT_TYPE_REQUEST = 200
        val PICK_LOCATION_REQUEST = 201
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_format)
        Places.initialize(this, "AIzaSyBU__lLKmiKrylBwRrGQH7nfnYSmkelAMM", Locale("vi","VN"))

        stepViewLayout = findViewById(R.id.step_view_layout)
        stepView = stepViewLayout.findViewById(R.id.step_view)
        stepView.go(1,true)

        typeSport = findViewById(R.id.typeSportEdit)
        locationInput = findViewById(R.id.locationInput)
        locationInput.isFocusable = false

        locationInput.setOnClickListener {
            var fieldList = arrayListOf<Place.Field>(Place.Field.ADDRESS)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(this)
            startActivityForResult(intent, PICK_LOCATION_REQUEST)
        }
        typeSport.setOnClickListener {
            val intent = Intent(this, SelectSportActivity::class.java)
            startActivityForResult(intent, PICK_SPORT_TYPE_REQUEST)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_LOCATION_REQUEST && resultCode == RESULT_OK){
            var place = Autocomplete.getPlaceFromIntent(data)
            locationInput.setText(place.address)
        }
        if(requestCode == PICK_SPORT_TYPE_REQUEST && resultCode == RESULT_OK){
            var typeSportStr = data!!.getStringExtra("type")
            typeSport.setText(typeSportStr)
        }
    }

}