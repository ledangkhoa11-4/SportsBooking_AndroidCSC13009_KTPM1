package com.example.sportbooking

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.sportbooking.Adapters.RatingRecyclerViewAdapter
import com.example.sportbooking.Adapters.ServiceAvaiRecyclerViewAdapter
import com.example.sportbooking.DTO.RatingCourt
import com.example.sportbooking.Ultils.CreateToast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import java.text.SimpleDateFormat
import java.util.*

class DetailCourtActivity : AppCompatActivity() {
    lateinit var viewPager:ViewPager2
    lateinit var dot:DotsIndicator

    lateinit var typeSportImage:ImageView;
    lateinit var courtName:TextView
    lateinit var courtLocation:TextView
    lateinit var courtPhone:TextView
    lateinit var courtPrice:TextView
    lateinit var hourServiceTv:TextView
    lateinit var weekdaysServiceTv:TextView
    lateinit var listServiceRv: RecyclerView
    lateinit var courtDetail:Court
    lateinit var favoriteBtn:ImageButton
    lateinit var messageBtn:ImageView
    var ratingList: ArrayList<RatingCourt> = ArrayList()
    var ratingResult: Float = 0.0f
    lateinit var ratingView: ListView
    var ratingAdapter: RatingRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_court)

        val intent = intent
        val index = intent.getIntExtra("index",0)
        courtDetail = HomeActivity.courtList_Home!![index]
        viewPager = findViewById(R.id.listImageDetailVP)
        dot = findViewById(R.id.dots_indicator)

        val config = CheckoutConfig(
            application = application,
            clientId = "AffNujh4pTllt3ynC_YK56rayRgTrcsPODYyT5Mb3EeOSp5tvQ6z8oHPQF3BlL3304kF8hihW3u7lC3i",
            environment = Environment.SANDBOX,
            returnUrl = "${BuildConfig.APPLICATION_ID}://paypalpay",
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true
            )
        )
        PayPalCheckout.setConfig(config)

        val vpAdapter = ImageViewPagerAdapter(courtDetail.bitmapArrayList)
        viewPager.setPageTransformer(MarginPageTransformer(37));
        viewPager.adapter = vpAdapter
        dot.attachTo(viewPager)

        courtName = findViewById(R.id.courtNameDetailBooking)
        courtName.setText(courtDetail.Name)

        typeSportImage = findViewById(R.id.typeSportImage)
        val drawableID = resources.getIdentifier("${courtDetail.Type.lowercase()}_icon","drawable",packageName)
        typeSportImage.setImageDrawable(resources.getDrawable(drawableID))
        courtLocation = findViewById(R.id.courtLocationDetailBooking)
        courtLocation.setText(courtDetail.location!!.addressName)
        courtLocation.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        courtPhone =  findViewById(R.id.courtPhone)
        courtPhone.setText(courtDetail.Phone)
        courtPrice =  findViewById(R.id.rentPriceDetailBooking)
        courtPrice.setText(formatPrice(courtDetail.Price))
        hourServiceTv = findViewById(R.id.hourServiceTv)
        hourServiceTv.setText(convertTimestampToTime(courtDetail.ServiceHour[0]) + " - " + convertTimestampToTime(courtDetail.ServiceHour[1]))
        weekdaysServiceTv = findViewById(R.id.weekdaysServiceTv)
        var serviceWeekdaysStr = courtDetail.ServiceWeekdays
        if(serviceWeekdaysStr[serviceWeekdaysStr.length-1]==',')
            serviceWeekdaysStr = serviceWeekdaysStr.substring(0,serviceWeekdaysStr.length-1)
        serviceWeekdaysStr = serviceWeekdaysStr.replace(",",", ")
        weekdaysServiceTv.setText(serviceWeekdaysStr)
        listServiceRv = findViewById(R.id.listServiceRv)
        val adapter = ServiceAvaiRecyclerViewAdapter(packageName,resources,courtDetail.AvalableService)
        listServiceRv.adapter = adapter
        listServiceRv.layoutManager = GridLayoutManager(this,2)
        loadRatingList()
        Log.i("printrating",ratingList.size.toString())
        ratingView = findViewById(R.id.listRating)
        val scrollView:ScrollView = findViewById(R.id.scrollView3)
        ratingView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                scrollView.requestDisallowInterceptTouchEvent(true)
                val action = event.actionMasked
                when (action) {
                    MotionEvent.ACTION_UP -> scrollView.requestDisallowInterceptTouchEvent(false)
                }
                return false
            }
        })
        ratingAdapter = RatingRecyclerViewAdapter(this,ratingList)
        ratingView.adapter = ratingAdapter

        courtLocation.setOnClickListener {
            val location =courtLocation.text.toString()
            val uri = Uri.parse("geo:0,0?q=$location")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        courtPhone.setOnClickListener {
            val phoneNumber =courtPhone.text.toString()
            val uri = Uri.parse("tel:$phoneNumber")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        findViewById<ImageButton>(R.id.backButtonRating).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.ViewScheduleBtn).setOnClickListener {
            val intent = Intent(this, CourtScheduleActivity::class.java)
            intent.putExtra("index",index)
            startActivity(intent);
        }
        findViewById<Button>(R.id.BookBtn).setOnClickListener {
            val intent = Intent(this, Booking::class.java)
            intent.putExtra("index",index)
            startActivity(intent)
        }
        favoriteBtn = findViewById(R.id.favoriteBtn)
        getFavoriteStatus()
        favoriteBtn.setOnClickListener {
            courtDetail.isFavorite = !courtDetail.isFavorite
            updateFavoriteStatus(courtDetail.isFavorite)
        }
        messageBtn=findViewById(R.id.messageBtn)
        messageBtn.setOnClickListener {
            val intent=Intent(this,ChatWithOwnerActivity::class.java)
            intent.putExtra("Name",courtDetail.Name)
            intent.putExtra("IDReceive",courtDetail.OwnerID)
            startActivity(intent)
        }
    }
    fun changeFavoriteStatus(){
        var drawable:Drawable
        if(courtDetail.isFavorite == true)
            drawable = resources.getDrawable(R.drawable.favorite)
        else
            drawable = resources.getDrawable(R.drawable.un_favorite)
        favoriteBtn.setImageDrawable(drawable)
    }
    fun formatPrice(price: Int): String {
        val formatter = java.text.DecimalFormat("#,###")
        return formatter.format(price) + "đ / 60min"
    }
    fun convertTimestampToTime(timestamp: Long): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(date)
    }

    fun getFavoriteStatus(){
        val favoriteRef = MainActivity.database.getReference("Favorite")
        val queryRef = favoriteRef.orderByChild("id").equalTo(MainActivity.user.id)
        queryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                courtDetail.isFavorite = false
                for(ds in snapshot.children){
                    if(ds.child("courtID").getValue(String::class.java).equals(courtDetail.CourtID)){
                        courtDetail.isFavorite = true
                        break
                    }
                }
                changeFavoriteStatus()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun updateFavoriteStatus(checked:Boolean){
        Log.i("AAAAAAAAAA",checked.toString())
        val favoriteRef = MainActivity.database.getReference("Favorite")
        if(checked == true){
            val newFavorite = HashMap<String,String>()
            newFavorite["courtID"] = courtDetail.CourtID
            newFavorite["id"] = MainActivity.user.id
            favoriteRef.push().setValue(newFavorite)
            CreateToast.createToast(this,"Hooray","This court will be in your wishlist",true)
            return
        }else{
            val queryRef = favoriteRef.orderByChild("id").equalTo(MainActivity.user.id)
            queryRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(ds in snapshot.children){
                        if(ds.child("courtID").getValue(String::class.java).equals(courtDetail.CourtID)){
                            val keyRef = ds.key
                            favoriteRef.child(keyRef!!).removeValue()
                            CreateToast.createToast(this@DetailCourtActivity,"Hooray","This court was removed from your wishlist",true)
                            break
                        }
                    }
                    changeFavoriteStatus()
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    fun loadRatingList() {
        val ratingRef = MainActivity.database.getReference("Rating");
        val queryRef = ratingRef.orderByChild("courtID").equalTo(courtDetail.CourtID)
        queryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var sum = 0.0f
                ratingList.clear()
                for (ds in dataSnapshot.children) {
                    val rating = ds.getValue(RatingCourt::class.java)
                    if(rating!!.id != null && rating!!.id != ""){
                        var imageRef = MainActivity.storageRef.child("user${rating.id}")
                        val ONE_MEGABYTE: Long = 1024 * 1024 * 5
                        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                            rating.userImage = bitmap
                            if (ratingAdapter != null) {
                                ratingAdapter!!.notifyDataSetChanged()
                            }
                        }.addOnFailureListener {
                            // Handle any errors
                        }
                    }
                    ratingList.add(rating!!)
                    sum += rating.star!!
                }
                ratingResult = sum/ratingList.size
                if(ratingAdapter != null){
                    if(ratingList.size > 0){
                        ratingView.scaleY = 1f
                        ratingView.background = null
                    }
                    ratingAdapter!!.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}