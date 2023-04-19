package com.example.sportbooking

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.sportbooking.DTO.User
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


class EditInformationActivity : AppCompatActivity() {
    lateinit var changeImgBtn: ImageButton
    lateinit var fullNameInput: EditText
    lateinit var genderInput: Spinner
    lateinit var dobInput: EditText
    lateinit var phoneInput: EditText
    lateinit var avatarIv: CircleImageView
    lateinit var backBtn:ImageButton
    lateinit var saveBtn:Button
    val currentUser: User = MainActivity.user
    var bitmapSelected:Bitmap? = null
    var selectedDate:Long = 0L
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
        avatarIv = findViewById(R.id.avatarTv)
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
                    .setTitleText("Select your birthday")
                    .build()

            datePicker.show(supportFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener {
                selectedDate = datePicker.selection!!
                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                var dob = Date(selectedDate!!)
                dobInput.setText(dateFormat.format(dob))
            }
        }

        if(currentUser.Image != null){
            avatarIv.setImageBitmap(currentUser.Image)
        }
        fullNameInput.setText(currentUser.username)
        when(currentUser.Gender){
            "Male"-> genderInput.setSelection(0)
            "Female"-> genderInput.setSelection(1)
            "Other"-> genderInput.setSelection(2)
        }
        if(currentUser.Dob != 0L){
            dobInput.setText(convertDate(currentUser.Dob))
        }
        phoneInput.setText(currentUser.Phone)
        saveBtn = findViewById(R.id.saveBtn)
        saveBtn.setOnClickListener {
            if(fullNameInput.text.toString() != "")
                currentUser.username = fullNameInput.text.toString()
            if(selectedDate != 0L)
                currentUser.Dob = selectedDate
            currentUser.Gender = genderList[genderInput.selectedItemId.toInt()]
            if(phoneInput.text.toString() != "")
                currentUser.Phone = phoneInput.text.toString()

            if(bitmapSelected != null){
                uploadUserImage(bitmapSelected!!)
                currentUser.Image = bitmapSelected
            }
            updateUserInfo()
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
            bitmapSelected = MediaStore.Images.Media.getBitmap(contentResolver, data?.data)
        }
        if(requestCode == TAKE_IMAGE_REQUEST && resultCode == RESULT_OK){
            val img = (data?.extras!!.get("data")) as Bitmap
            avatarIv.setImageBitmap(img)
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
    fun convertDate(timeStamp:Long):String{
        val sdf = SimpleDateFormat("dd/MM/yyyy") // create a SimpleDateFormat object with desired format
        val formattedTime = sdf.format(Date(timeStamp)) // convert timestamp to date and format as string
        return formattedTime
    }
    fun uploadUserImage(bitmap: Bitmap):String{
        var pathStr = "user${currentUser.id}"
        val baos = ByteArrayOutputStream()
        val Ref = MainActivity.storageRef.child(pathStr)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = Ref.putBytes(data)
        uploadTask.addOnFailureListener {
            pathStr = ""
        }.addOnSuccessListener { taskSnapshot ->
        }
        return pathStr
    }
    fun updateUserInfo(){
        val userRef = MainActivity.database.getReference("User")
        val userUpdates = HashMap<String, Any>()
        userUpdates["id"] = currentUser.id
        userUpdates["email"] = currentUser.email
        userUpdates["username"] = currentUser.username
        userUpdates["Dob"] = currentUser.Dob
        userUpdates["Gender"] = currentUser.Gender
        userUpdates["Phone"] = currentUser.Phone

        userRef.orderByChild("id").equalTo(currentUser.id).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children) {
                    val keyId = userSnapshot.key
                    userRef.child(keyId!!).setValue(userUpdates)
                    createToast("Hooray","Your information was saved!",true);
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
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
}