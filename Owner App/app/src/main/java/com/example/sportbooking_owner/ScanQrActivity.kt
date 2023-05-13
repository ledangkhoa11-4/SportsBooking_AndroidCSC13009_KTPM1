package com.example.sportbooking_owner

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.IOException

class ScanQrActivity : AppCompatActivity(), Detector.Processor<Barcode> {
    private lateinit var cameraSource: CameraSource
    private lateinit var surfaceView: SurfaceView
    private lateinit var detector: Detector<Barcode>
    companion object{
        val REQUEST_CAMERA_PERMISSION = 209
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)

        findViewById<ImageButton>(R.id.backButtonBookingView).setOnClickListener {
            finish()
        }
        surfaceView = findViewById(R.id.surfaceView)
        detector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()
        detector.setProcessor(this)
        cameraSource = CameraSource.Builder(this, detector)
            .setRequestedPreviewSize(1600, 1024)
            .setAutoFocusEnabled(true)
            .build()

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            this@ScanQrActivity,
                            android.Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this@ScanQrActivity,
                            arrayOf(android.Manifest.permission.CAMERA),
                            REQUEST_CAMERA_PERMISSION
                        )
                        return
                    }
                    cameraSource.start(surfaceView.holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                // Do nothing
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

    }

    override fun release() {
        //do notthing
    }

    override fun receiveDetections(p0: Detector.Detections<Barcode>) {
        val barcodes = p0.detectedItems
        if (barcodes.size() > 0) {
            val qrCode = barcodes.valueAt(0).displayValue
            runOnUiThread {

                updateStatus(qrCode)
                finish()
            }
        }
    }
    override fun onPause() {
        super.onPause()
        cameraSource.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        detector.release()
        cameraSource.release()
    }
    fun updateStatus(secret:String){
        val secretID = secret.toDoubleOrNull()
        Log.i("BBBBBBBBBBBBB",secretID.toString())
        if(secretID == null){
            MotionToast.createToast(this,
                "Error",
                "QR code value not correct format",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            return
        }
        val bookingRef = MainActivity.database.getReference("Booking")
        val query = bookingRef.orderByChild("secretID").equalTo(secretID)
        query.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    bookingRef.child(ds.key!!).child("status").setValue(true).addOnSuccessListener {
                        MotionToast.createToast(this@ScanQrActivity,
                            "Successfully",
                            "Checked-in user successfully",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@ScanQrActivity, www.sanju.motiontoast.R.font.helvetica_regular))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}