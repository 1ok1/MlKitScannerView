# MlKitScannerView
MlKitScannerView uses Camera2 and Google Vision library to scan the Qr code


> Step1: Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
> Step2: Add the dependency to app build.gradle
```
    implementation 'com.github.1ok1:MlKitScannerView:main-SNAPSHOT'
```
> Step3: Add MlKitScannerView to your xml
```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <com.ikol.barcode.MlKitScannerView
        android:id="@+id/mlKitScannerView"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```
> Step4: Add the integration code to the Activity or fragment

```
    // Declare MlKitScannerView
    var mlKitScannerView: MlKitScannerView? = null
    
    //OnCreate
    mlKitScannerView = findViewById(R.id.mlKitScannerView)

    // Will create the camera source and return the qr code when the scan is complete
    fun createCameraSource() {
        mlKitScannerView?.createCameraSource(this, object : ScanInfo {
            override fun qrCode(qrCode: String) {
                Log.d(TAG, "<<qrCode>>: $qrCode")
            }
        })
    }
```

> Step5: Call ```createCameraSource()``` on ```onCreate()```

> Step6: Add ```mlKitScannerView?.onResume()``` on ```onResume()```

> Step7: Add ```mlKitScannerView?.onPause()``` on ```onPause()```

> Step8: To add Camera Permission
```
    companion object {
        const val PERMISSION_REQUEST_CAMERA = 1
    }
    
    private fun haveCameraPermission(): Boolean {
        return if (Build.VERSION.SDK_INT < 23) true else checkSelfPermission(
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ...
        if (!haveCameraPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
            }
        } else {
            createCameraSource()
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CAMERA -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createCameraSource()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
```

> Options: 
> ```mlKitScannerView?.showInfo()``` to show debug frame info
> ```mlKitScannerView?.hideInfo()``` to hide debug frame info
> ```mlKitScannerView?.showDetection()``` to show the detection frame with value
> ```mlKitScannerView?.hideDetection()``` to hide the detection frame with value
> ```mlKitScannerView?.stopCameraSource()``` Stops the camera source