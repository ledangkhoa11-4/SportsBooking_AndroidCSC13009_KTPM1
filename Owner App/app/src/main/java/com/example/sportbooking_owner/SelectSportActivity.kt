package com.example.sportbooking_owner

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

class SportListViewAdapter(private val context:Activity, private val listItem:ArrayList<String>):ArrayAdapter<String>(context, R.layout.sport_listview,listItem){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView: View = inflater.inflate(R.layout.sport_listview, null, true)
        val sportNameTv = rowView.findViewById<TextView>(R.id.sportNameTv)
        val sportImage: ImageView = rowView.findViewById(R.id.sportIconIv) as ImageView

        val sportName = listItem[position].lowercase()
        sportNameTv.setText(listItem[position])
        val resourceId =  context.resources.getIdentifier("${sportName}_icon", "drawable",context.getPackageName());
        sportImage.setImageResource(resourceId)
        return rowView
    }
}
class SelectSportActivity : AppCompatActivity() {
    lateinit var sportsList:ArrayList<String>
    lateinit var sportListView:ListView
    lateinit var searchInput:AutoCompleteTextView
    lateinit var sportListFilterd:ArrayList<String>
    lateinit var adapter:SportListViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_sport)
        sportsList = arrayListOf("Archery","Badminton","Basketball","Football","Futsal","Gym","Rugby","Tennis","Voleyball")
        searchInput = findViewById(R.id.searchSportInput)
        sportListView = findViewById(R.id.listSportLv)
        sportListFilterd = ArrayList<String>()
        sportListFilterd.addAll(sportsList)
        adapter = SportListViewAdapter(this, sportListFilterd)
        sportListView.adapter = adapter

        sportListView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = intent
            intent.putExtra("type",sportListFilterd[i])
            setResult(RESULT_OK,intent)
            finish()
        }
        val autoCompleteAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sportsList)
        searchInput!!.setAdapter(autoCompleteAdapter)
        searchInput!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                sportListFilterd.clear()
                for(sportName in sportsList){
                    if(sportName.contains(p0!!,true))
                        sportListFilterd.add(sportName)
                }
                adapter.notifyDataSetChanged()
            }
        })
    }
}