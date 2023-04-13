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
import android.view.View
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class UpdateCourtActivity : AppCompatActivity() {
    var chooseImageBtn:ImageButton?=null
    var courtNameEdt:EditText?=null
    var courtDesEdt:EditText?=null
    var typeEdt:EditText?=null
    var typeIcon:ImageView?=null
    var listUri=ArrayList<Uri>()
    var listBitmap=ArrayList<Bitmap>()
    var imageVP2_Update: ViewPager2?=null
    lateinit var tM:ToggleButton
    lateinit var tTue:ToggleButton
    lateinit var tW:ToggleButton
    lateinit var tT:ToggleButton
    lateinit var tF:ToggleButton
    lateinit var tS:ToggleButton
    lateinit var tSu:ToggleButton
    lateinit var weekdaysChoice:String

    lateinit var timeStart:EditText
    lateinit var timeEnd:EditText
    var hourStart:Int = 0
    var minuteStart:Int = 0
    var hourEnd:Int = 22
    var minuteEnd:Int = 0
    lateinit var dot:DotsIndicator
    lateinit var toolbar: Toolbar
    lateinit var PriceEdt:EditText
    lateinit var DesEdt:EditText
    lateinit var FreeParking:CheckBox
    lateinit var FreeWifi:CheckBox
    lateinit var ShoeRent:CheckBox
    lateinit var Referees:CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_court)
        chooseImageBtn=findViewById(R.id.chooseImageBtn)
        courtNameEdt=findViewById(R.id.CourtNameEdt)
        courtDesEdt=findViewById(R.id.descriptionEdt)
        typeEdt=findViewById(R.id.TypeEdt)
        typeIcon=findViewById(R.id.TypeIcon)
        imageVP2_Update=findViewById(R.id.imageVP2_Update)
        DesEdt=findViewById(R.id.descriptionEdt)
        FreeParking=findViewById(R.id.freeParkCb2)
        FreeWifi=findViewById(R.id.freeWifiCb2)
        ShoeRent=findViewById(R.id.shoesCb2)
        Referees=findViewById(R.id.refereesCb2)

        timeStart=findViewById(R.id.timeStartPickerEdt)
        timeEnd=findViewById(R.id.timeEndPickerEdt)
        toolbar=findViewById(R.id.toolbar2)
        PriceEdt=findViewById(R.id.PriceEdt)
        dot=findViewById<DotsIndicator>(R.id.dots_indicator)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        //load data from database to view
        val courtpos=intent.getIntExtra("pos",0)
        val court=SignIn.listCourt[courtpos]

        courtNameEdt!!.setText(court!!.Name)

        typeEdt!!.setText(court.Type)
        var imageName=court.Type.lowercase()+"_icon"
        val imageResource=resources.getIdentifier(imageName,"drawable",packageName)
        if (imageResource != 0) { // Check if the image resource was found
            typeIcon!!.setImageResource(imageResource)
        } else {
            // Image resource not found, handle error or show default image
        }

        val start=convertTimestampToTime(court.ServiceHour[0])
        var temp=start.split(":")
        hourStart=temp[0].toInt()
        minuteStart=temp[1].toInt()
        val end=convertTimestampToTime(court.ServiceHour[1])
         temp=end.split(":")
        hourEnd=temp[0].toInt()
        minuteEnd=temp[1].toInt()
        timeStart.setText(start)
        timeEnd.setText(end)
        PriceEdt.setText(formatPrice(court.Price))

        val datePickerView:View = findViewById(R.id.daypicker)
         tM = datePickerView.findViewById(R.id.tM) as ToggleButton
         tTue = datePickerView.findViewById(R.id.tTue) as ToggleButton
         tW = datePickerView.findViewById(R.id.tW) as ToggleButton
         tT = datePickerView.findViewById(R.id.tT) as ToggleButton
         tF = datePickerView.findViewById(R.id.tF) as ToggleButton
         tS = datePickerView.findViewById(R.id.tS) as ToggleButton
         tSu = datePickerView.findViewById(R.id.tSu) as ToggleButton
        val weekday=court.ServiceWeekdays.split(",")
        for(i in weekday.indices){
            if(weekday[i]=="Mon"){
                tM.isChecked=true
            }
            if(weekday[i]=="Tue"){
                tTue.isChecked=true
            }
            if(weekday[i]=="Wed"){
                tW.isChecked=true
            }
            if(weekday[i]=="Thu"){
                tT.isChecked=true
            }
            if(weekday[i]=="Fri"){
                tF.isChecked=true
            }
            if(weekday[i]=="Sat"){
                tS.isChecked=true
            }
            if(weekday[i]=="Sun"){
                tSu.isChecked=true
            }
        }

        DesEdt.setText(court.Description)

        for(i in court.AvalableService.indices){
            if(court.AvalableService[i].lowercase()=="Free Parking".lowercase()){
                FreeParking.isChecked=true
            }
            if(court.AvalableService[i].lowercase()=="Free wifi".lowercase()){
                FreeWifi.isChecked=true
            }
            if(court.AvalableService[i].lowercase()=="Shoes for rent".lowercase()){
                ShoeRent.isChecked=true
            }
            if(court.AvalableService[i].lowercase()=="Referees available".lowercase()){
                Referees.isChecked=true
            }

        }


        //view page slider
        val vpa=ImageViewPagerAdapter(court.bitmapArrayList)
        imageVP2_Update!!.adapter = vpa
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer(ViewPager2.PageTransformer { page, position ->
            page.setOnClickListener {
                showBottomSheetDialog()
            }
        })
        imageVP2_Update!!.setPageTransformer(transformer)
        dot.attachTo(imageVP2_Update!!)
        chooseImageBtn!!.setOnClickListener {
            showBottomSheetDialog()
        }
        typeEdt!!.setOnClickListener {
            var intent= Intent(this,SelectSportActivity::class.java)
            startActivityForResult(intent,NewFormatActivity.PICK_SPORT_TYPE_REQUEST)
        }


        //Time picker
        val startPicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hourStart)
                .setMinute(minuteStart)
                .setTitleText("Select opening time")
                .build()
        val endPicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hourEnd)
                .setMinute(minuteEnd)
                .setTitleText("Select closing time")
                .build()
        startPicker.addOnPositiveButtonClickListener {
            var time = "";
            if(timeEnd.text.toString().length != 0){

                if((hourEnd+minuteEnd/60.0) - (startPicker.hour+startPicker.minute/60.0) <= 0){
                    Toast.makeText(this, "Opening time must earlier than Closing time", Toast.LENGTH_SHORT).show()
                    timeStart.setText("")
                }else
                    if((hourEnd+minuteEnd/60.0) - (startPicker.hour+startPicker.minute/60.0) < 2){
                        Toast.makeText(this, "Operating time must at least 2 hour", Toast.LENGTH_SHORT).show()
                        timeStart.setText("")
                    }else{
                        time = startPicker.hour.toString().padStart(2,'0') + ":" +startPicker.minute.toString().padStart(2,'0')
                        timeStart.setText(time)
                        hourStart = startPicker.hour
                        minuteStart = startPicker.minute
                    }

            }else{
                time = startPicker.hour.toString().padStart(2,'0') + ":" +startPicker.minute.toString().padStart(2,'0')
                timeStart.setText(time)
                hourStart = startPicker.hour
                minuteStart = startPicker.minute
            }
        }
        endPicker.addOnPositiveButtonClickListener {
            var time = "";
            if(timeStart.text.toString().length != 0){
                if((endPicker.hour+endPicker.minute/60.0) - (hourStart+minuteStart/60.0) <= 0){
                    Toast.makeText(this, "Opening time must earlier than Closing time", Toast.LENGTH_SHORT).show()
                    timeEnd.setText("")
                }else
                    if((endPicker.hour+endPicker.minute/60.0) - (hourStart+minuteStart/60.0) < 2){
                        Toast.makeText(this, "Operating time must at least 2 hour ", Toast.LENGTH_SHORT).show()
                        timeEnd.setText("")
                    }else{
                        time = endPicker.hour.toString().padStart(2,'0') + ":" +endPicker.minute.toString().padStart(2,'0')
                        timeEnd.setText(time)
                        hourEnd = endPicker.hour
                        minuteEnd = endPicker.minute
                    }
            }else{
                time = endPicker.hour.toString().padStart(2,'0') + ":" +endPicker.minute.toString().padStart(2,'0')
                timeEnd.setText(time)
                hourEnd = endPicker.hour
                minuteEnd = endPicker.minute
            }
        }
        timeStart.setOnClickListener {
            startPicker.show(supportFragmentManager, "tag");
        }
        timeEnd.setOnClickListener {
            endPicker.show(supportFragmentManager, "tag");
        }

    }
    fun formatPrice(price: Int): String {
        val formatter = java.text.DecimalFormat("#,###")
        return formatter.format(price) + "đ / 60min"
    }
    fun convertFromToggoleBtnPickedDayToString(){
        weekdaysChoice=""
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
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == NewFormatActivity.PICK_SPORT_TYPE_REQUEST && resultCode == RESULT_OK){
            var typeSportStr = data!!.getStringExtra("type")
            typeEdt!!.setText(typeSportStr)
            var imageName=typeSportStr!!.lowercase()+"_icon"
            val imageResource=resources.getIdentifier(imageName,"drawable",packageName)
            if (imageResource != 0) { // Check if the image resource was found
                typeIcon!!.setImageResource(imageResource)
            } else {
                // Image resource not found, handle error or show default image
            }


        }
        if (requestCode == NewInfoActivity.PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            listUri=ArrayList<Uri>()
            if (data?.clipData != null) {
                val clipData = data.clipData
                for (i in 0 until clipData?.itemCount!!) {
                    listUri.add(clipData.getItemAt(i).uri)
                    val inputStream = applicationContext.contentResolver.openInputStream(clipData.getItemAt(i).uri)
                    listBitmap.add(BitmapFactory.decodeStream(inputStream))
                }
            } else {
                listUri.add(data?.data!!)
                val inputStream = applicationContext.contentResolver.openInputStream(data?.data!!)
                listBitmap.add(BitmapFactory.decodeStream(inputStream))
            }
            imageVP2_Update!!.adapter = SliderImageAdapter(listUri)
            chooseImageBtn!!.setImageResource(0)
            chooseImageBtn!!.visibility= View.GONE

        }
        if (requestCode == NewInfoActivity.TAKE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            val img = (data?.extras!!.get("data")) as Bitmap
            listBitmap.add(img)
            imageVP2_Update!!.adapter = SliderImageAdapter(arrayListOf(Uri.EMPTY), img)

        }
        dot.attachTo(imageVP2_Update!!)
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
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
            NewInfoActivity.PICK_IMAGE_REQUEST
        );
    }

    fun takeImageCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, NewInfoActivity.TAKE_IMAGE_REQUEST)
    }

}