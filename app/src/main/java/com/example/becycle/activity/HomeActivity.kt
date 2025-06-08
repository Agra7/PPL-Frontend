package com.example.becycle.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.becycle.R
import com.example.becycle.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications
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
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private var imageCapture: ImageCapture? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize UI components
        previewView = findViewById(R.id.camera_preview)
        scanButton = findViewById(R.id.button_scan)
        switchCameraButton = findViewById(R.id.button_switch_camera)
        galleryButton = findViewById(R.id.button_gallery)
        loadingOverlay = findViewById(R.id.loading_overlay)

        setupBottomNavIfNeeded()
        setupPermissions()

        outputDirectory = getOutputDirectory()

        // Setup button listeners
        scanButton.setOnClickListener { takePhoto() }
        switchCameraButton.setOnClickListener { toggleCamera() }
        galleryButton.setOnClickListener { openGallery() }
    }

    private fun setupPermissions() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissionsLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }

    private fun toggleCamera() {
        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK)
            CameraSelector.LENS_FACING_FRONT
        else
            CameraSelector.LENS_FACING_BACK
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                scanButton.isEnabled = true
                hideLoading()
            } catch (exc: Exception) {
                Log.e("CameraX", "Use case binding failed", exc)
                Toast.makeText(this, "Failed to start camera.", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val capture = imageCapture ?: return
        loadingOverlay.visibility = View.VISIBLE

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
                    hideLoading()
                    Toast.makeText(baseContext, "Photo capture failed.", Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Log.e("AnalyzeImage", "Exception")
                    analyzeImage(output.savedUri ?: photoFile.toUri())
                    Log.e("AnalyzeImage", "Exception")
                }
            }
        )
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    loadingOverlay.visibility = View.VISIBLE
                    analyzeImage(uri)
                }
            }
        }

    private fun analyzeImage(uri: Uri) {
        try {

            Log.e("AnalyzeImage", "Exception")
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
//                    runOnUiThread {
//                        hideLoading()
//                        Toast.makeText(this@HomeActivity, "Error: $error", Toast.LENGTH_SHORT).show()
//                    }
                    Log.e("AnalyzeImage", "Exception")
                }

                override fun onResults(results: List<Classifications>?) {
                    runOnUiThread {
                        hideLoading()
                        results?.let {
                            val topResult = it.firstOrNull()?.categories?.maxByOrNull { category -> category.score }
                            if (topResult != null) {
                                Log.e("AnalyzeImage1121", "Exception123")
                                openResultActivity(uri, topResult.label, topResult.score)
                                Log.e("AnalyzeImage1121", "${topResult.label}")
                            } else {
                                openResultActivity(uri, "Not found", 0.0f)
                            }
                        } ?: openResultActivity(uri, "Not found", 0.0f)
                    }
                }
            }
        )
            Log.e("AnalyzeImage", "Exception")
        imageClassifierHelper.classifyStaticImage(uri)
            Log.e("AnalyzeImage", "Exception")
        } catch (e: Exception) {
            Log.e("AnalyzeImage", "Exception: ${e.message}", e)
            hideLoading()
            Toast.makeText(this, "Analyze crash: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openResultActivity(imageUri: Uri, label: String, score: Float) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_IMAGE_URI, imageUri.toString())
            putExtra(ResultActivity.EXTRA_LABEL, label)
            putExtra(ResultActivity.EXTRA_SCORE, score)
        }
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this, scanButton, "shared_image"
        )
        startActivity(intent, options.toBundle())
    }

    private fun hideLoading() {
        loadingOverlay.animate()
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                loadingOverlay.visibility = View.GONE
                loadingOverlay.alpha = 1f
            }
            .start()
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted.", Toast.LENGTH_SHORT).show()
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