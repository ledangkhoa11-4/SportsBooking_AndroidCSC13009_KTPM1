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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog


class UpdateCourtActivity : AppCompatActivity() {
    var chooseImageBtn:ImageButton?=null
    var courtNameEdt:EditText?=null
    var courtDesEdt:EditText?=null
    var typeEdt:EditText?=null
    var listUri=ArrayList<Uri>()
    var listBitmap=ArrayList<Bitmap>()
    var imageVP2_Update: ViewPager2?=null
    var weekDaysPickerEdt:EditText?=null
    var weekdaysChoice=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_court)
        chooseImageBtn=findViewById(R.id.chooseImageBtn) as ImageButton
        courtNameEdt=findViewById(R.id.CourtNameEdt) as EditText
        courtDesEdt=findViewById(R.id.descriptionEdt) as EditText
        typeEdt=findViewById(R.id.TypeEdt) as EditText
        imageVP2_Update=findViewById(R.id.imageVP2_Update) as ViewPager2
        weekDaysPickerEdt=findViewById(R.id.WeekDaysEdt) as EditText
        val court=intent.getParcelableExtra<Courts>("UpdateCourt")
        courtNameEdt!!.setText(court!!.Name)

        typeEdt!!.setText(court.Type)
        //view page slider
        imageVP2_Update!!.adapter = SliderImageAdapter(arrayListOf(Uri.EMPTY))
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer(ViewPager2.PageTransformer { page, position ->
            page.setOnClickListener {
                showBottomSheetDialog()
            }
        })
        imageVP2_Update!!.setPageTransformer(transformer)
        chooseImageBtn!!.setOnClickListener {
            showBottomSheetDialog()
        }
        typeEdt!!.setOnClickListener {
            var intent= Intent(this,SelectSportActivity::class.java)
            startActivityForResult(intent,NewFormatActivity.PICK_SPORT_TYPE_REQUEST)
        }
        //Day picker
        weekDaysPickerEdt!!.setOnClickListener {
            showWeekdayPickerDialog()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == NewFormatActivity.PICK_SPORT_TYPE_REQUEST && resultCode == RESULT_OK){
            var typeSportStr = data!!.getStringExtra("type")
            typeEdt!!.setText(typeSportStr)
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

    private fun showWeekdayPickerDialog() {
        val datePickerView = layoutInflater.inflate(R.layout.weekdays_picker_dialog, null)
        var tM = datePickerView.findViewById(R.id.tM) as ToggleButton
        var tTue = datePickerView.findViewById(R.id.tTue) as ToggleButton
        var tW = datePickerView.findViewById(R.id.tW) as ToggleButton
        var tT = datePickerView.findViewById(R.id.tT) as ToggleButton
        var tF = datePickerView.findViewById(R.id.tF) as ToggleButton
        var tS = datePickerView.findViewById(R.id.tS) as ToggleButton
        var tSu = datePickerView.findViewById(R.id.tSu) as ToggleButton

        var dialog:AlertDialog?=null
        val builder=AlertDialog.Builder(this)

        builder.setView(datePickerView)
        builder.setTitle("A dialog with a list of options")
        builder
            .setPositiveButton("Done", DialogInterface.OnClickListener { dialog, id ->
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
                weekDaysPickerEdt!!.setText(weekdaysChoice)
// FIRE ZE MISSILES!
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
// User cancelled the dialog
            })
        dialog=builder.create()






        dialog!!.show()
    }

    fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
            NewInfoActivity.PICK_IMAGE_REQUEST
        );
    }

    fun takeImageCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, NewInfoActivity.TAKE_IMAGE_REQUEST)
    }
    class WeekdaysPicker : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {

// Use the Builder class for convenient dialog construction
                val builder = AlertDialog.Builder(it)
                val datePickerView = layoutInflater.inflate(R.layout.weekdays_picker, null)
                var tM = datePickerView.findViewById(R.id.tM) as ToggleButton
                var tTue = datePickerView.findViewById(R.id.tTue) as ToggleButton
                var tW = datePickerView.findViewById(R.id.tW) as ToggleButton
                var tT = datePickerView.findViewById(R.id.tT) as ToggleButton
                var tF = datePickerView.findViewById(R.id.tF) as ToggleButton
                var tS = datePickerView.findViewById(R.id.tS) as ToggleButton
                var tSu = datePickerView.findViewById(R.id.tSu) as ToggleButton
                var weekdaysChoice = ""

// Create the AlertDialog object and return it
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}