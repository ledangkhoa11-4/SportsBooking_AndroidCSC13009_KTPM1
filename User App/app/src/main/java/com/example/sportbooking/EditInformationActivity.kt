package com.example.sportbooking

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*


class EditInformationActivity : AppCompatActivity() {
    lateinit var changeImgBtn: ImageButton
    lateinit var fullNameInput: EditText
    lateinit var genderInput: Spinner
    lateinit var dobInput: EditText
    lateinit var phoneInput: EditText
    lateinit var avatarIv: CircleImageView
    lateinit var backBtn:ImageButton
    companion object{
        val PICK_IMAGE_REQUEST = 201
        val TAKE_IMAGE_REQUEST = 202
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_information)

        val genderList = arrayListOf<String>("Male", "Female", "Other")
        changeImgBtn = findViewById(R.id.changeAvtBtn)
        fullNameInput = findViewById(R.id.fullnameInput)
        genderInput = findViewById(R.id.genderSpinner)
        dobInput = findViewById(R.id.dobInput)
        phoneInput = findViewById(R.id.phoneInput)
        avatarIv = findViewById(R.id.avatarIv)
        backBtn = findViewById(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }
        changeImgBtn.setOnClickListener {
           showBottomSheetDialog()

        }
        ArrayAdapter(this, android.R.layout.simple_spinner_item, genderList)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                genderInput.adapter = adapter
            }
        dobInput.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .build()

            datePicker.show(supportFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener {
                val selectedDate = datePicker.selection
                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                var dob = Date(selectedDate!!)
                dobInput.setText(dateFormat.format(dob))
            }
        }

    }
    fun pickImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    fun takeImageCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, TAKE_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK){
            avatarIv.setImageURI(data?.data)
        }
        if(requestCode == TAKE_IMAGE_REQUEST && resultCode == RESULT_OK){
            val img = (data?.extras!!.get("data")) as Bitmap
            avatarIv.setImageBitmap(img)
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
}