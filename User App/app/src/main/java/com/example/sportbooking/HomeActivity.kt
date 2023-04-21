package com.example.sportbooking

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.sportbooking.Adapters.homeListViewAdapter
import com.example.sportbooking.DTO.LocationManager
import com.example.sportbooking.Ultils.GetDistance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import com.mancj.materialsearchbar.MaterialSearchBar
import com.nex3z.togglebuttongroup.MultiSelectToggleGroup
import java.text.Normalizer
import java.util.*

class HomeActivity : AppCompatActivity(),MaterialSearchBar.OnSearchActionListener {
    lateinit var nav_bar: NavigationBarView
    lateinit var listView: ListView
    lateinit var locationManager: LocationManager
    lateinit var searchBar:MaterialSearchBar
    private val lastSearches: List<String>? = null
    lateinit var filterBtn:FloatingActionButton
    private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()
    companion object{
        var listViewAdapter: homeListViewAdapter? = null
        var lastCourList:ArrayList<Court>? = null
        var courtList_Home:ArrayList<Court>? = null
        fun updateDistance(){
            for(court in courtList_Home!!){
                if(MainActivity.lastLocation.latLng.latitude != 0.0 && MainActivity.lastLocation.latLng.longitude != 0.0){
                    court.courtDistance = GetDistance.getDistance(MainActivity.lastLocation,court.location!!).toDouble()
                }
            }
            listViewAdapter?.notifyDataSetChanged()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        nav_bar = findViewById(R.id.nav_bar)
        navBarHandle(nav_bar)
        locationManager = LocationManager(this);
        locationManager.startLocationUpdates()
        listView = findViewById(R.id.homeListView)
        listView.divider = null
        courtList_Home = MainActivity.listCourt

        listViewAdapter = homeListViewAdapter(this,courtList_Home!!)
        listView.adapter = listViewAdapter
        listView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, DetailCourtActivity::class.java)
            intent.putExtra("index",i)
            startActivity(intent)
        }
        val filterGroup = findViewById<MultiSelectToggleGroup>(R.id.groupFilter)

        filterGroup.setOnCheckedChangeListener { group, checkedId, isChecked ->
            clearAllToggle(checkedId, isChecked);
            when(checkedId) {
                R.id.distanceFilter -> filterByDistance(isChecked)
                R.id.priceFilter -> filterByPrice(isChecked)
                R.id.bookMostTgBtn -> filterByNumBook(isChecked)
                R.id.ratingFilter -> filterByRating(isChecked)
            }
        }
        searchBar = findViewById(R.id.searchBar)
        searchBar.setOnSearchActionListener(this)

        filterBtn = findViewById(R.id.filterBtn)
        filterBtn.setOnClickListener {
            val intent = Intent(this, SearchStadiumActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }
    fun clearAllToggle(except:Int, isChecked:Boolean){
        findViewById<ToggleButton>(R.id.distanceFilter).isChecked = false;
        findViewById<ToggleButton>(R.id.bookMostTgBtn).isChecked = false;
        findViewById<ToggleButton>(R.id.ratingFilter).isChecked = false;
        findViewById<ToggleButton>(R.id.priceFilter).isChecked = false;
        if(except == R.id.distanceFilter)
            findViewById<ToggleButton>(R.id.distanceFilter).isChecked = isChecked;
        if(except == R.id.bookMostTgBtn)
            findViewById<ToggleButton>(R.id.bookMostTgBtn).isChecked = isChecked;
        if(except == R.id.ratingFilter)
            findViewById<ToggleButton>(R.id.ratingFilter).isChecked = isChecked;
        if(except == R.id.priceFilter)
            findViewById<ToggleButton>(R.id.priceFilter).isChecked = isChecked;

    }
    fun filterByDistance(isFilter:Boolean){
        if(isFilter == true){
            if(lastCourList == null){
                lastCourList = ArrayList<Court>();
                lastCourList!!.addAll(courtList_Home!!.toList())
            }
            locationManager.stopLocationUpdates()
            locationManager.startLocationUpdates()
            Collections.sort(courtList_Home,DistanceComparator())
        }else{
            courtList_Home!!.clear()
            courtList_Home!!.addAll(lastCourList!!.toList())
        }
        listViewAdapter!!.notifyDataSetChanged()
    }
    fun filterByPrice(isFilter:Boolean){
        if(isFilter == true){
            if(lastCourList == null){
                lastCourList = ArrayList<Court>();
                lastCourList!!.addAll(courtList_Home!!.toList())
            }
            Collections.sort(courtList_Home,PriceComparator())
        }else{
            courtList_Home!!.clear()
            courtList_Home!!.addAll(lastCourList!!.toList())
        }
        listViewAdapter!!.notifyDataSetChanged()
    }
    fun filterByNumBook(isFilter:Boolean){
        if(isFilter == true){
            if(lastCourList == null){
                lastCourList = ArrayList<Court>();
                lastCourList!!.addAll(courtList_Home!!.toList())
            }
            Collections.sort(courtList_Home,NumBookComparator())
        }else{
            courtList_Home!!.clear()
            courtList_Home!!.addAll(lastCourList!!.toList())
        }
        listViewAdapter!!.notifyDataSetChanged()
    }
    fun filterByRating(isFilter:Boolean){
        if(isFilter == true){
            if(lastCourList == null){
                lastCourList = ArrayList<Court>();
                lastCourList!!.addAll(courtList_Home!!.toList())
            }
            Collections.sort(courtList_Home,RatingComparator())
        }else{
            courtList_Home!!.clear()
            courtList_Home!!.addAll(lastCourList!!.toList())
        }
        listViewAdapter!!.notifyDataSetChanged()
    }
    fun navBarHandle(nav_bar: NavigationBarView){
        nav_bar.selectedItemId = R.id.item_home
        nav_bar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_home-> true
                R.id.item_user->{
                    startActivity(Intent(this,UserTabActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.item_schedule->{
                    startActivity(Intent(this,MyBookingActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationManager.handleOnRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSearchStateChanged(enabled: Boolean) {

    }

    override fun onSearchConfirmed(text: CharSequence?) {
        if(lastCourList == null){
            lastCourList = ArrayList<Court>();
            lastCourList!!.addAll(courtList_Home!!.toList())
        }
        courtList_Home!!.clear()
        val keywork = unaccent(text.toString()).lowercase()
        for(court in lastCourList!!){
            val courtName = unaccent(court.Name).lowercase()
            if(courtName.contains(keywork,true))
                courtList_Home!!.add(court)
        }
        listViewAdapter!!.notifyDataSetChanged()
    }

    override fun onButtonClicked(buttonCode: Int) {
        TODO("Not yet implemented")
    }

    fun unaccent(str:String): String {
        val temp = Normalizer.normalize(str, Normalizer.Form.NFD)
        return REGEX_UNACCENT.replace(temp, "")
    }

}