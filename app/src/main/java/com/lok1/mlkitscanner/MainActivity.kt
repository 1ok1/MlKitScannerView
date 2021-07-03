package com.lok1.mlkitscanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.lok1.barcode.MlKitScannerView
import com.lok1.barcode.ScanInfo

class MainActivity : AppCompatActivity(), ScanInfo {

    companion object {
        const val PERMISSION_REQUEST_CAMERA = 1
        const val TAG = "MainActivity"
    }

    var mlKitScannerView: MlKitScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mlKitScannerView = findViewById(R.id.mlKitScannerView)
        mlKitScannerView?.hideInfo()
        mlKitScannerView?.showDetection()
        if (!haveCameraPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
            }
        } else {
            mlKitScannerView?.createCameraSource(this, this)
        }
    }

    private fun haveCameraPermission(): Boolean {
        return if (Build.VERSION.SDK_INT < 23) true else checkSelfPermission(
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
        mlKitScannerView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mlKitScannerView?.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CAMERA -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mlKitScannerView?.createCameraSource(this, this)
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun qrCode(qrCode: String) {
        Log.d(TAG, "<<qrCode>>: $qrCode")
    }
}