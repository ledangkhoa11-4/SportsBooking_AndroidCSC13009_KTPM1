package com.example.sportbooking_owner

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
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
                // Update TextView with QR code
                Toast.makeText(this, qrCode,Toast.LENGTH_SHORT).show()
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
}