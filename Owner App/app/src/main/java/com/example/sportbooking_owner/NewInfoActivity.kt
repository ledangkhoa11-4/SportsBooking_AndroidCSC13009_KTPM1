package com.example.sportbooking_owner

import android.content.ClipDescription
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Layout
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shuhart.stepview.StepView

class NewInfoActivity : AppCompatActivity() {
    lateinit var stepViewLayout: View
    lateinit var stepView: StepView

    lateinit var chooseImageBtn: ImageButton
    lateinit var courtNameInput: EditText
    lateinit var descriptionInput: EditText
    lateinit var nextStep: Button
    var isImagePicked = false

    companion object {
        val PICK_IMAGE_REQUEST = 201
        val TAKE_IMAGE_REQUEST = 202
        val FORMAT_STEP_REQUEST = 203
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_info)

        stepViewLayout = findViewById(R.id.step_view_layout)
        stepView = stepViewLayout.findViewById(R.id.step_view)
        stepView.go(0, true)

        chooseImageBtn = findViewById(R.id.imageButton)

        chooseImageBtn.setOnClickListener {
            showBottomSheetDialog()
        }
        courtNameInput = findViewById(R.id.courtNameInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        nextStep = findViewById(R.id.nextStepBtn)
        nextStep.setOnClickListener {
//            if(!isImagePicked || courtNameInput.text.length == 0 || descriptionInput.text.length == 0){
//                Toast.makeText(this, "Please enter all information to continue!",Toast.LENGTH_SHORT).show()
//            }else{
            val intent = Intent(this, NewFormatActivity::class.java)
            startActivityForResult(intent, FORMAT_STEP_REQUEST)
            overridePendingTransition(0, 0)
            //}
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

    fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    fun takeImageCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, TAKE_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            chooseImageBtn.setImageURI(data?.data)
            isImagePicked = true
            chooseImageBtn.setScaleType(ImageView.ScaleType.CENTER_CROP)
        }
        if (requestCode == TAKE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            val img = (data?.extras!!.get("data")) as Bitmap
            chooseImageBtn.setImageBitmap(img)
            isImagePicked = true
            chooseImageBtn.setScaleType(ScaleType.CENTER_CROP)
        }
    }
}