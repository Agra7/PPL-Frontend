package com.example.becycle

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : BaseActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var scanButton: View
    private lateinit var switchCameraButton: View
    private lateinit var galleryButton: View
    private lateinit var outputDirectory: File
    private lateinit var loadingOverlay: View

    private var imageCapture: ImageCapture? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        previewView = findViewById(R.id.camera_preview)
        scanButton = findViewById(R.id.button_scan)
        switchCameraButton = findViewById(R.id.button_switch_camera)
        galleryButton = findViewById(R.id.button_gallery)

        loadingOverlay = findViewById(R.id.loading_overlay)
        loadingOverlay.visibility = View.VISIBLE
        scanButton.isEnabled = false

        outputDirectory = getOutputDirectory()

        // Disable scan button until camera is ready
        scanButton.isEnabled = false

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissionsLauncher.launch(REQUIRED_PERMISSIONS)
        }

        scanButton.setOnClickListener {
            takePhoto()
        }

        switchCameraButton.setOnClickListener {
            lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK)
                CameraSelector.LENS_FACING_FRONT
            else
                CameraSelector.LENS_FACING_BACK
            startCamera()
        }

        galleryButton.setOnClickListener {
            openGallery()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val capture = ImageCapture.Builder().build()
            imageCapture = capture

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, capture
                )

                // Enable scan button and hide loading overlay
                scanButton.isEnabled = true

                loadingOverlay.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction {
                        loadingOverlay.visibility = View.GONE
                        loadingOverlay.alpha = 1f // Reset for next time
                    }
                    .start()

            } catch (exc: Exception) {
                Log.e("CameraX", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }


    private fun takePhoto() {
        val capture = imageCapture ?: run {
            Log.w("CameraX", "ImageCapture is not initialized")
            return
        }

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                .format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        capture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("CameraX", "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    openResultActivity(photoFile.absolutePath)
                }
            }
        )
    }

    private fun openResultActivity(imagePath: String) {
        val intent = Intent(this@HomeActivity, ResultActivity::class.java).apply {
            putExtra("image_path", imagePath)
        }
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@HomeActivity, scanButton, "shared_image"
        )
        startActivity(intent, options.toBundle())
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                selectedImageUri?.let {
                    val intent = Intent(this@HomeActivity, ResultActivity::class.java).apply {
                        putExtra("image_uri", it.toString())
                    }
                    startActivity(intent)
                }
            }
        }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                finish()
            }
        }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
