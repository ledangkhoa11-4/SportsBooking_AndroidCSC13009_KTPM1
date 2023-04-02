package com.example.sportbooking

import android.app.Activity
import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView

class History(var time:String?,var nameEvent:String?,var fieldType:String?,var address:String?){
}
class BookingHistory : AppCompatActivity() {
    var histories = arrayListOf<History>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_history)
        createData()
        var view = findViewById<ListView>(R.id.customListView)
        val adapter = MyListAdapter(this,histories)
        view!!.adapter = adapter
    }
    fun createData(){
        histories.add(History("17:30 - 19:00, Mar 5, 2023","Tennis Court Booking","Tennis","204A Mai Chi Tho, An phu Ward, District 2"))
        histories.add(History("20:30 - 22:00, Mar 3, 2023","Football Field Booking","Football","204A Mai Chi Tho, An phu Ward, District 2"))
    }
}
class MyListAdapter
    (
    private val context: Activity, private val maintitles: ArrayList<History>,
) : ArrayAdapter<History>(context, R.layout.custom_list_view, maintitles) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView: View = inflater.inflate(R.layout.custom_list_view, null, true)
        val time = rowView.findViewById(R.id.timeTextView) as TextView
        val myEvent = rowView.findViewById(R.id.myEventTextView) as TextView
        val nameEvent = rowView.findViewById(R.id.nameEventTextView) as TextView
        val fieldType = rowView.findViewById(R.id.FieldTypeTextView) as TextView
        val address = rowView.findViewById(R.id.AddressTextView) as TextView
        val imageView: ImageView = rowView.findViewById(R.id.imageView) as ImageView
        time.text = maintitles[position].time
        myEvent.text = "My Event"
        nameEvent.text = maintitles[position].nameEvent
        fieldType.text = maintitles[position].fieldType
        address.text = maintitles[position].address
        imageView.setImageResource(R.drawable.all_sport_icon)
        return rowView
    }
}