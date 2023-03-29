package com.example.sportbooking_owner

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shuhart.stepview.StepView

class NewInfoActivity : AppCompatActivity() {
    lateinit var stepViewLayout: View
    lateinit var stepView: StepView
    lateinit var chooseImageBtn: ImageButton
    lateinit var courtNameInput: EditText
    lateinit var descriptionInput: EditText
    lateinit var nextStep: Button
    lateinit var imageSliderVP2:ViewPager2
    var isImagePicked = false

    companion object {
        val PICK_IMAGE_REQUEST = 201
        val TAKE_IMAGE_REQUEST = 202
        val FORMAT_STEP_REQUEST = 203
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_info)

        imageSliderVP2 = findViewById(R.id.imageSliderVP2)
        stepViewLayout = findViewById(R.id.step_view_layout)
        stepView = stepViewLayout.findViewById(R.id.step_view)
        stepView.go(0, true)

        chooseImageBtn = findViewById(R.id.imageButton)

        imageSliderVP2.adapter = SliderImageAdapter(arrayListOf(Uri.EMPTY))
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer(ViewPager2.PageTransformer { page, position ->
            page.setOnClickListener {
                showBottomSheetDialog()
            }
        })
        imageSliderVP2.setPageTransformer(transformer)
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
        findViewById<FloatingActionButton>(R.id.backBtn).setOnClickListener {
            val intent = intent
            setResult(RESULT_CANCELED,intent)
            finish()
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
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
    }

    fun takeImageCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, TAKE_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            isImagePicked = true
            var listUri = ArrayList<Uri>()
            if (data?.clipData != null) {
                val clipData = data.clipData
                for (i in 0 until clipData?.itemCount!!) {
                    listUri.add(clipData.getItemAt(i).uri)
                }
            } else {
                listUri.add(data?.data!!)
            }
            imageSliderVP2.adapter = SliderImageAdapter(listUri)
            chooseImageBtn.setImageResource(0)

        }
        if (requestCode == TAKE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            val img = (data?.extras!!.get("data")) as Bitmap
            imageSliderVP2.adapter = SliderImageAdapter(arrayListOf(Uri.EMPTY), img)
            isImagePicked = true
        }
    }
}