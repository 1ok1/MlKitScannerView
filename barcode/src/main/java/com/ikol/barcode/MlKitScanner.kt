package com.ikol.barcode

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.ikol.barcode.barcodescanner.BarcodeScannerProcessor
import com.ikol.barcode.common.CameraSource
import com.ikol.barcode.common.CameraSourcePreview
import com.ikol.barcode.common.GraphicOverlay
import com.ikol.barcode.common.PreferenceUtils
import java.io.IOException

class MlKitScannerView : FrameLayout {
    private val TAG = this.javaClass.name

    var cameraSource: CameraSource? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var preview: CameraSourcePreview? = null


    constructor(context: Context) : super(context) {
        inflate(context, R.layout.mlkit_scanner, this)
        initViews()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        inflate(context, R.layout.mlkit_scanner, this)
        initViews()
    }

    private fun initViews() {
        graphicOverlay = findViewById(R.id.fireFaceOverlay)
        preview = findViewById(R.id.firePreview)
    }

    fun showInfo() {
        PreferenceUtils.showDetectionInfo(context)
    }

    fun hideInfo() {
        PreferenceUtils.hideDetectionInfo(context)
    }

    fun showDetection() {
        PreferenceUtils.showDetection(context)
    }

    fun hideDetection() {
        PreferenceUtils.hideDetection(context)
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private fun startCameraSource() {
        cameraSource?.let {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null")
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null")
                }
                preview?.start(it, graphicOverlay)
            } catch (e: IOException) {
                Log.e(TAG, "Unable to start camera source.", e)
                cameraSource?.release()
                cameraSource = null
            }
        }
    }

    fun createCameraSource(activity: AppCompatActivity, scanInfo: ScanInfo) {
        if (cameraSource == null) {
            cameraSource = CameraSource(activity, graphicOverlay)
        }
        cameraSource?.setMachineLearningFrameProcessor(BarcodeScannerProcessor(activity, scanInfo))
        startCameraSource()
    }

    /**
     * starts the camera when camera source start is called
     */
    fun resumeCamera() {
        val handler = Handler()
        handler.postDelayed({ startCameraSource() }, 2000)
    }

    /**
     * To be called on OnPause of activity to stop camera source
     */
    fun onPause() {
        stopCameraSource()
    }

    /**
     * Stops the camera source
     */
    fun stopCameraSource() {
        cameraSource?.stop()
    }

    /**
     * To be called on onResume to start the camera source
     */
    fun onResume() {
        startCameraSource()
    }
}