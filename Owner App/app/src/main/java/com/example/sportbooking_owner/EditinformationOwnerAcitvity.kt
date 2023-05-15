package com.example.sportbooking_owner

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

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
    lateinit var saveBtn:Button
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
        if(owner.Image!=null){
            bitmapSelected=owner.Image
        }
        selectedGender=owner.Gender

        val genderOptions = arrayOf("Male", "Female", "Other")
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

        saveBtn=findViewById(R.id.saveBtn)
        saveBtn.setOnClickListener {
            if(nameEdt.text.toString()!="" && selectedDate!=0L && selectedGender!="" && phoneEdt.text.toString()!="" && bitmapSelected!=null) {
                upLoadUserImage(bitmapSelected!!)
                upLoadUserInformation()
            }
            else{
                createToast("Error","Please fill in every field in this form", false)
            }
        }
    }
    fun upLoadUserImage(bitmap: Bitmap):String{
        val baos=ByteArrayOutputStream()
        var path="owner${owner.id}"
        val ref=MainActivity.storageRef.child(path)
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos)
        val data=baos.toByteArray()

        val uploadTask=ref.putBytes(data).addOnCompleteListener{

        }.addOnFailureListener{
            path=""
        }
        return path
    }
    fun upLoadUserInformation(){
        val user_update= HashMap<String, Any>()
        user_update["id"]=owner.id
        user_update["username"]=nameEdt.text.toString()
        user_update["email"]=emailEdt.text.toString()
        user_update["phone"]=phoneEdt.text.toString()
        user_update["dob"]=selectedDate
        user_update["gender"]=selectedGender


        val userRef=MainActivity.database.getReference("Owner")
        val queryRef=userRef.orderByChild("id").equalTo(owner.id)
        queryRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(childsnap in snapshot.children){
                    val key=childsnap.key
                    if (key != null) {
                        userRef.child(key).setValue(user_update)
                    }
                    createToast("Success", "Your changes have been saved",true)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    fun createToast(title:String, message:String, isSuccess:Boolean){
        var style: MotionToastStyle
        if(isSuccess)
            style = MotionToastStyle.SUCCESS
        else
            style = MotionToastStyle.ERROR
        MotionToast.createToast(this,
            title,
            message,
            style,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
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