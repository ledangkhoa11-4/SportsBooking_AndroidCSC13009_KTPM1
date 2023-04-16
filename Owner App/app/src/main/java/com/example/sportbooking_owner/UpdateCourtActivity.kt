package com.example.sportbooking_owner

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import nl.joery.timerangepicker.TimeRangePicker
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class UpdateCourtActivity : AppCompatActivity() {

    lateinit var chooseImageBtn: ImageButton
    lateinit var imageVP2_Update: ViewPager2
    lateinit var dots_indicator: DotsIndicator

    lateinit var listUri: ArrayList<Uri>
    var listBitmap: kotlin.collections.ArrayList<Bitmap> = ArrayList()
    lateinit var CourtNameEdt: EditText
    lateinit var descriptionEdt: EditText
    lateinit var TypeEdt: EditText
    lateinit var TypeIcon: ImageView
    lateinit var PriceEdt: EditText
    lateinit var LocationEdt: EditText
    var newLocation: Location? = null

    lateinit var tM: ToggleButton
    lateinit var tTue: ToggleButton
    lateinit var tW: ToggleButton
    lateinit var tT: ToggleButton
    lateinit var tF: ToggleButton
    lateinit var tS: ToggleButton
    lateinit var tSu: ToggleButton
    var weekdaysChoice: String = ""
    lateinit var datePickerView: View
    lateinit var timeChoose: TextView
    lateinit var timeRangePicker: TimeRangePicker
    lateinit var timeStart: TextView
    lateinit var timeEnd: TextView
    var start = -1L
    var end = -1L
    lateinit var yardNumberPicker:com.shawnlin.numberpicker.NumberPicker

    companion object {
        val PICK_LOCATION_REQUEST = 201
        val PICK_IMAGE_REQUEST = 202
        val TAKE_IMAGE_REQUEST = 203
        val PICK_SPORT_TYPE_REQUEST = 204
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_court)
        //get court
        val courtpos = intent.getIntExtra("pos", 0)
        val court = SignIn.listCourt[courtpos]


        //image handle
        chooseImageBtn = findViewById(R.id.chooseImageBtn)
        imageVP2_Update = findViewById(R.id.imageVP2_Update)
        dots_indicator = findViewById(R.id.dots_indicator)


        imageVP2_Update.adapter =
            SliderImageAdapter(arrayListOf(Uri.EMPTY), null, court.bitmapArrayList)
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer(ViewPager2.PageTransformer { page, position ->
            page.setOnClickListener {
                showBottomSheetDialog()
            }
        })
        imageVP2_Update.setPageTransformer(transformer)
        dots_indicator.attachTo(imageVP2_Update)
        chooseImageBtn.setOnClickListener {
            showBottomSheetDialog()
        }

        CourtNameEdt = findViewById(R.id.CourtNameEdt)
        CourtNameEdt.setText(court.Name)
        descriptionEdt = findViewById(R.id.descriptionEdt)
        descriptionEdt.setText(court.Description)
        TypeEdt = findViewById(R.id.TypeEdt)
        TypeIcon = findViewById(R.id.TypeIcon)
        TypeEdt.setText(court.Type)
        var imageName = court.Type!!.lowercase() + "_icon"
        val imageResource = resources.getIdentifier(imageName, "drawable", packageName)
        TypeIcon!!.setImageResource(imageResource)
        TypeEdt.setOnClickListener {
            val intent = Intent(this, SelectSportActivity::class.java)
            startActivityForResult(intent, PICK_SPORT_TYPE_REQUEST)
        }
        PriceEdt = findViewById(R.id.PriceEdt)
        PriceEdt.setText(court.Price.toString())
        LocationEdt = findViewById(R.id.LocationEdt)
        LocationEdt.setText(court.location?.addressName)

        Places.initialize(this, MainActivity.apiPlace, Locale("vi", "VN"))
        LocationEdt.setOnClickListener {
            var fieldList = arrayListOf<Place.Field>(Place.Field.ADDRESS, Place.Field.LAT_LNG)
            val intent =
                Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                    .setCountries(listOf("VN"))
                    .build(this)
            startActivityForResult(intent, PICK_LOCATION_REQUEST)
        }
        datePickerView = findViewById(R.id.daypicker)
        tM = datePickerView.findViewById(R.id.tM)
        tTue = datePickerView.findViewById(R.id.tTue)
        tW = datePickerView.findViewById(R.id.tW)
        tT = datePickerView.findViewById(R.id.tT)
        tF = datePickerView.findViewById(R.id.tF)
        tS = datePickerView.findViewById(R.id.tS)
        tSu = datePickerView.findViewById(R.id.tSu)



        if(court.ServiceWeekdays.contains("Mon",false)) tM.isChecked = true
        if(court.ServiceWeekdays.contains("Tue",false)) tTue.isChecked = true
        if(court.ServiceWeekdays.contains("Wed",false)) tW.isChecked = true
        if(court.ServiceWeekdays.contains("Thu",false)) tT.isChecked = true
        if(court.ServiceWeekdays.contains("Fri",false)) tF.isChecked = true
        if(court.ServiceWeekdays.contains("Sat",false)) tS.isChecked = true
        if(court.ServiceWeekdays.contains("Sun",false)) tSu.isChecked = true

        val pickTimeView: View =
            LayoutInflater.from(this).inflate(R.layout.time_picker_layout, null);
        timeRangePicker = pickTimeView.findViewById(R.id.timeRangePicker)
        timeRangePicker.startTime = TimeRangePicker.Time(0, 0)
        timeStart = pickTimeView.findViewById(R.id.startTimeTv)
        timeEnd = pickTimeView.findViewById(R.id.endTimeTv)
        timeRangePicker.setOnTimeChangeListener(object : TimeRangePicker.OnTimeChangeListener {
            override fun onStartTimeChange(startTime: TimeRangePicker.Time) {
                timeStart.text =
                    startTime.hour.toString().padStart(2, '0') + ":" + startTime.minute.toString()
                        .padStart(2, '0')
                start = timeStringToTimestamp(timeStart.text.toString())
            }

            override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {}

            override fun onEndTimeChange(endTime: TimeRangePicker.Time) {
                timeEnd.text =
                    endTime.hour.toString().padStart(2, '0') + ":" + endTime.minute.toString()
                        .padStart(2, '0')
                end = timeStringToTimestamp(timeEnd.text.toString())
            }

        })
        timeStart.text = "${convertTimestampToTime(court.ServiceHour[0])}"
        timeEnd.text = "${convertTimestampToTime(court.ServiceHour[1])}"
        start = timeStringToTimestamp(timeStart.text.toString())
        end = timeStringToTimestamp(timeEnd.text.toString())
        timeRangePicker.startTimeMinutes = timeToMinute(timeStart.text.toString())
        timeRangePicker.endTimeMinutes = timeToMinute(timeEnd.text.toString())

        timeChoose = findViewById(R.id.timeChoose)
        timeChoose.setText(
            "${convertTimestampToTime(court.ServiceHour[0])} - ${
                convertTimestampToTime(
                    court.ServiceHour[1]
                )
            } "
        )
        timeChoose.setOnClickListener {
            if (pickTimeView.parent != null) {
                var vg = pickTimeView.parent as ViewGroup
                vg.removeView(pickTimeView)
            }
            MaterialAlertDialogBuilder(this)
                .setTitle("Pick booking time")
                .setView(pickTimeView)
                .setPositiveButton("Apply") { dialog, which ->
                    timeStart.text = timeRangePicker.startTime.hour.toString()
                        .padStart(2, '0') + ":" + timeRangePicker.startTime.minute.toString()
                        .padStart(2, '0')
                    start = timeStringToTimestamp(timeStart.text.toString())
                    timeEnd.text = timeRangePicker.endTime.hour.toString()
                        .padStart(2, '0') + ":" + timeRangePicker.endTime.minute.toString()
                        .padStart(2, '0')
                    end = timeStringToTimestamp(timeEnd.text.toString())
                    timeChoose.setText(timeStart.text.toString() + " - " + timeEnd.text.toString())
                }.show()
        }
        yardNumberPicker = findViewById(R.id.yardNumberPicker)
        yardNumberPicker.value = court.numOfYards

        if(court.AvalableService.contains("Free parking"))findViewById<CheckBox>(R.id.freeParkCb2).isChecked = true
        if(court.AvalableService.contains("Free wifi"))findViewById<CheckBox>(R.id.freeWifiCb2).isChecked = true
        if(court.AvalableService.contains("Referees available"))findViewById<CheckBox>(R.id.refereesCb2).isChecked = true
        if(court.AvalableService.contains("Shoes for rent")) findViewById<CheckBox>(R.id.shoesCb2).isChecked = true

        findViewById<Button>(R.id.UpdateBtn).setOnClickListener {
            var newBitmapList = listBitmap
            if(newBitmapList.size == 0){
                newBitmapList = court.bitmapArrayList
            }
            var newName = CourtNameEdt.text.toString()
            var newDes = descriptionEdt.text.toString()
            var newType = TypeEdt.text.toString()
            var newPrice = PriceEdt.text.toString().toInt()
            var newLoc = newLocation
            if(newLoc == null)
                newLoc = court.location
            weekdaysChoice = ""
            if(tM.isChecked()){
                weekdaysChoice +="Mon,";
            }
            if(tTue.isChecked()){
                weekdaysChoice +="Tue,";
            }
            if(tW.isChecked()){
                weekdaysChoice +="Wed,";
            }
            if(tT.isChecked()){
                weekdaysChoice +="Thu,";
            }
            if(tF.isChecked()){
                weekdaysChoice +="Fri,";
            }
            if(tS.isChecked()){
                weekdaysChoice +="Sat,";
            }
            if(tSu.isChecked()){
                weekdaysChoice +="Sun";
            }

            var newServiceTime = arrayListOf<Long>(start,end)
            var newYard = yardNumberPicker.value
            val availableService=ArrayList<String>()
            if(findViewById<CheckBox>(R.id.freeParkCb2).isChecked)
                availableService.add("Free parking")
            if(findViewById<CheckBox>(R.id.freeWifiCb2).isChecked)
                availableService.add("Free wifi")
            if(findViewById<CheckBox>(R.id.refereesCb2).isChecked)
                availableService.add("Referees available")
            if(findViewById<CheckBox>(R.id.shoesCb2).isChecked)
                availableService.add("Shoes for rent")


            val updatedCourt = Courts(court.CourtID, court.OwnerID, newName, newType, newLoc, court.Phone , weekdaysChoice, newServiceTime, court.Images,
                ArrayList(),newDes,availableService,newPrice,newYard)
            val courtRef=MainActivity.database.getReference("Courts")
            val queryRef=courtRef.orderByChild("courtID").equalTo(court.CourtID)
            queryRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(courtSnapshot in  snapshot.children){
                        val key =  courtSnapshot.key
                        if (key != null) {
                            if(listBitmap.size>0){
                                for(i in court.Images.indices){
                                    val imageRef=MainActivity.storageRef.child(court.Images[i])
                                    imageRef.delete().addOnCompleteListener {  }.addOnFailureListener {
                                        Log.e("upImage","del failed",)
                                    }
                                }
                                var taskCount = listBitmap.size
                                court.Images.clear()
                                for(i in listBitmap.indices){
                                    val calendar=Calendar.getInstance()
                                    var path="image${calendar.timeInMillis}"
                                    val baos = ByteArrayOutputStream()
                                    val Ref = MainActivity.storageRef.child(path)
                                    court.Images.add(path)
                                    listBitmap[i].compress(Bitmap.CompressFormat.PNG, 100, baos)
                                    val data = baos.toByteArray()

                                    var uploadTask = Ref.putBytes(data)
                                    uploadTask.addOnFailureListener {

                                    }.addOnSuccessListener { taskSnapshot ->
                                        taskCount--
                                        if(taskCount == 0)
                                            queryRef.ref.child(key).setValue(updatedCourt)
                                    }
                                }
                            }else{
                                queryRef.ref.child(key).setValue(updatedCourt)
                            }

                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
            MotionToast.createToast(this,
                "Update court successfully",
                "Court updated successfully!",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))

            SignIn.listCourt[courtpos] = updatedCourt
            finish()


        }
        findViewById<ImageButton>(R.id.backButtonUpdate).setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_SPORT_TYPE_REQUEST && resultCode == RESULT_OK) {
            var typeSportStr = data!!.getStringExtra("type")
            TypeEdt!!.setText(typeSportStr)
            var imageName = typeSportStr!!.lowercase() + "_icon"
            val imageResource = resources.getIdentifier(imageName, "drawable", packageName)
            if (imageResource != 0) { // Check if the image resource was found
                TypeIcon!!.setImageResource(imageResource)
            } else {
                // Image resource not found, handle error or show default image
            }
        }
        if (requestCode == PICK_LOCATION_REQUEST && resultCode == RESULT_OK) {
            var place = Autocomplete.getPlaceFromIntent(data)
            LocationEdt.setText(place.address)
            var latLng = LatLng(place.latLng.latitude, place.latLng.longitude)
            newLocation = Location(place.address, latLng)
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            listUri = ArrayList<Uri>()
            if (data?.clipData != null) {
                val clipData = data.clipData
                for (i in 0 until clipData?.itemCount!!) {
                    listUri.add(clipData.getItemAt(i).uri)
                    val inputStream =
                        applicationContext.contentResolver.openInputStream(clipData.getItemAt(i).uri)
                    listBitmap.add(BitmapFactory.decodeStream(inputStream))
                }
            } else {
                listUri.add(data?.data!!)
                val inputStream = applicationContext.contentResolver.openInputStream(data?.data!!)
                listBitmap.add(BitmapFactory.decodeStream(inputStream))
            }
            imageVP2_Update!!.adapter = SliderImageAdapter(listUri)
            chooseImageBtn!!.setImageResource(0)
            chooseImageBtn!!.visibility = View.GONE

        }
        if (requestCode == TAKE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            val img = (data?.extras!!.get("data")) as Bitmap
            listBitmap.add(img)
            imageVP2_Update!!.adapter = SliderImageAdapter(arrayListOf(Uri.EMPTY), img)

        }
        dots_indicator.attachTo(imageVP2_Update!!)
    }

    private fun showBottomSheetDialog() {
        val view = layoutInflater.inflate(R.layout.modal_bottom_sheet_content, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(view)
        val openGalleryButton = view.findViewById<Button>(R.id.pickGaleryBtn)
        val openCameraBtn = view.findViewById<Button>(R.id.pickCameraBtn)
        openGalleryButton.setOnClickListener {
            pickImageGallery()
            dialog.dismiss()
        }
        openCameraBtn.setOnClickListener {
            takeImageCamera()
            dialog.dismiss()
        }
        dialog.show()
    }

    fun convertTimestampToTime(timestamp: Long): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(date)
    }

    fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.action = Intent.ACTION_GET_CONTENT;
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE_REQUEST
        );
    }

    fun takeImageCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, TAKE_IMAGE_REQUEST)
    }
    fun timeToMinute(str:String):Int{
        val timeString = str
        val (hours, minutes) = timeString.split(":").map { it.toInt() }
        return hours * 60 + minutes
    }
    fun timeStringToTimestamp(timeString: String): Long {
        val format = SimpleDateFormat("hh:mm", Locale.getDefault())
        val date = format.parse(timeString)
        return date?.time ?: 0
    }

}