package com.example.sportbooking_owner

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shuhart.stepview.StepView
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.ByteArrayOutputStream
import java.util.Calendar

class NewInfoActivity : AppCompatActivity() {
    lateinit var stepViewLayout: View
    lateinit var stepView: StepView
    lateinit var chooseImageBtn: ImageButton
    lateinit var courtNameInput: EditText
    lateinit var descriptionInput: EditText
    lateinit var nextStep: Button
    lateinit var imageSliderVP2: ViewPager2
    lateinit var bitmapList:ArrayList<Bitmap>
    lateinit var listUri:ArrayList<Uri>
    var isImagePicked = false


    companion object {
        val PICK_IMAGE_REQUEST = 201
        val TAKE_IMAGE_REQUEST = 202
        val FORMAT_STEP_REQUEST = 203
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_info)

        bitmapList = ArrayList()
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
        nextStep = findViewById(R.id.finishBtn)
        nextStep.setOnClickListener {
            if (!isImagePicked || courtNameInput.text.length == 0 || descriptionInput.text.length == 0) {
                MotionToast.createToast(this,
                    "Warning",
                    "Please fill all the details!",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))

            } else {
                val intent = Intent(this, NewFormatActivity::class.java)
                startActivityForResult(intent, FORMAT_STEP_REQUEST)
                overridePendingTransition(0, 0)
            }
        }
        findViewById<FloatingActionButton>(R.id.backBtn).setOnClickListener {
            val intent = intent
            setResult(RESULT_CANCELED, intent)
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
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    fun takeImageCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, TAKE_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            isImagePicked = true
            listUri = ArrayList<Uri>()
            if (data?.clipData != null) {
                val clipData = data.clipData
                for (i in 0 until clipData?.itemCount!!) {
                    listUri.add(clipData.getItemAt(i).uri)
                    val inputStream = applicationContext.contentResolver.openInputStream(clipData.getItemAt(i).uri)
                    bitmapList.add(BitmapFactory.decodeStream(inputStream))
                }
            } else {
                listUri.add(data?.data!!)
                val inputStream = applicationContext.contentResolver.openInputStream(data?.data!!)
                bitmapList.add(BitmapFactory.decodeStream(inputStream))
            }
            imageSliderVP2.adapter = SliderImageAdapter(listUri)
            chooseImageBtn.setImageResource(0)
        }
        if (requestCode == TAKE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            val img = (data?.extras!!.get("data")) as Bitmap
            bitmapList.add(img)
            imageSliderVP2.adapter = SliderImageAdapter(arrayListOf(Uri.EMPTY), img)
            isImagePicked = true
        }
        if (requestCode == FORMAT_STEP_REQUEST && resultCode == RESULT_OK) {
           var newCourt = data!!.getParcelableExtra<Courts>("court")!!
           for(i in 0 until bitmapList.size){
               val imageID = uploadImage(bitmapList[i])
               if(imageID.length != 0)
                   newCourt.Images.add(imageID)
           }
            newCourt.Name = courtNameInput.text.toString()
            newCourt.Description = descriptionInput.text.toString()
            newCourt.OwnerID = SignIn.user!!.uid
            val Ref = MainActivity.database.getReference("Courts")
            Ref.push().setValue(newCourt)
            MotionToast.createToast(this,
                "Hurray success",
                "Add new court completed successfully!",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            finish()
        }
    }
    fun uploadImage(bitmap: Bitmap):String{
        val calendar = Calendar.getInstance()
        var pathStr = "image${calendar.timeInMillis}"
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
}