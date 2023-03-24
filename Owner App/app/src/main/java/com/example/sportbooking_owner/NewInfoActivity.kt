package com.example.sportbooking_owner

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Layout
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shuhart.stepview.StepView

class NewInfoActivity : AppCompatActivity() {
    lateinit var stepViewLayout: View
    lateinit var stepView:StepView

    lateinit var chooseImageBtn:ImageButton

    companion object{
        val PICK_IMAGE_REQUEST = 201
        val TAKE_IMAGE_REQUEST = 202
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_info)

        stepViewLayout = findViewById(R.id.step_view_layout)
        stepView = stepViewLayout.findViewById(R.id.step_view)
        stepView.go(0,true)

        chooseImageBtn = findViewById(R.id.imageButton)

        chooseImageBtn.setOnClickListener {
            showBottomSheetDialog()

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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        chooseImageBtn.setScaleType(ImageView.ScaleType.CENTER_CROP)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK){
            chooseImageBtn.setImageURI(data?.data)

        }
        if(requestCode == TAKE_IMAGE_REQUEST && resultCode == RESULT_OK){
            val img = (data?.extras!!.get("data")) as Bitmap
            chooseImageBtn.setImageBitmap(img)
        }
    }
}