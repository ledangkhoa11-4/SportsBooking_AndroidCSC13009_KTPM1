package com.example.sportbooking_owner

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class EditinformationOwnerAcitvity : AppCompatActivity() {
    lateinit var avartarImage:CircleImageView
    lateinit var chooseImageBtn:ImageButton
    lateinit var nameEdt:EditText
    lateinit var emailEdt:EditText
    lateinit var phoneEdt:EditText
    lateinit var dobEdt:EditText
    lateinit var genderSpinner:Spinner
    private var selectedDate:Long = 0L
    private var selectedGender:String=""
    private var bitmapSelected:Bitmap?=null
    val owner=SignIn.owner
    companion object{
        val PICK_IMAGE_REQUEST = 201
        val TAKE_IMAGE_REQUEST = 202
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editinformation_owner_acitvity)
        nameEdt=findViewById(R.id.userNameEdt)
        nameEdt.setText(owner.username)

        emailEdt=findViewById(R.id.emailEdit_Edt)
        emailEdt.setText(owner.email)

        phoneEdt=findViewById(R.id.phoneEdt_edt)
        phoneEdt.setText(owner.Phone)

        selectedGender=owner.Gender
        val genderOptions = arrayOf("Male", "Female", "Non-Binary", "Other")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner = findViewById<Spinner>(R.id.GenderSpinner)
        genderSpinner.adapter = adapter
        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                 selectedGender = genderOptions[position]
                // Do something with the selected gender
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when no gender is selected
            }
        }
        genderSpinner.setSelection(genderOptions.indexOf(selectedGender))

        selectedDate=owner.Dob
        dobEdt=findViewById(R.id.DobEdit_Edt)
        dobEdt.setText(convertLongToDateString(selectedDate))
        dobEdt.setOnClickListener {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select your birthday")
                        .build()

                datePicker.show(supportFragmentManager, "tag")
                datePicker.addOnPositiveButtonClickListener {
                    selectedDate = datePicker.selection!!

                    dobEdt.setText(convertLongToDateString(selectedDate))
            }

        }
        avartarImage=findViewById(R.id.avatarrEdditImV)
        avartarImage.setImageBitmap(owner.Image)

        chooseImageBtn=findViewById(R.id.choosseImageEdit_Btn)
        chooseImageBtn.setOnClickListener {
            showBottomSheetDialog()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK){
            avartarImage.setImageURI(data?.data)
            bitmapSelected = MediaStore.Images.Media.getBitmap(contentResolver, data?.data)
        }
        if(requestCode == TAKE_IMAGE_REQUEST && resultCode == RESULT_OK){
            val img = (data?.extras!!.get("data")) as Bitmap
            avartarImage.setImageBitmap(img)
            bitmapSelected = img
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

    fun pickImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    fun takeImageCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, TAKE_IMAGE_REQUEST)
    }

    fun convertLongToDateString(selectedDate:Long):String{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        var dob = Date(selectedDate!!)
        return dateFormat.format(dob)
    }
}