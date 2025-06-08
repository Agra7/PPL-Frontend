package com.example.becycle.helper

import android.content.Context
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import com.example.becycle.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.File

class ImageClassifierHelper(
    var threshold: Float = 0.5f,
    var maxResults: Int = 3,
    val modelName: String = "best_model_with_metadata_sequel.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {

    private val imageClassifier: ImageClassifier? by lazy { setupImageClassifier() }

    private fun setupImageClassifier(): ImageClassifier? {
        return try {
            val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
                .setScoreThreshold(threshold)
                .setMaxResults(maxResults)
                .setBaseOptions(BaseOptions.builder().setNumThreads(4).build())

            ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: Exception) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed) + ": " + e.message)
            Log.e(TAG, "TensorFlow Lite failed to initialize. Your model may be missing metadata. Error: ${e.message}")
            null
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        if (imageClassifier == null) {
            classifierListener?.onError("Image classifier is not initialized.")
            return
        }

        try {
            // Convert URI to a Bitmap
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // The ImageClassifier will now handle all resizing and normalization internally.
            val results = imageClassifier?.classify(TensorImage.fromBitmap(bitmap))

            classifierListener?.onResults(results)
        } catch (e: Exception) {
            classifierListener?.onError("Error during classification: ${e.message}")
            Log.e(TAG, "Classification failed", e)
        }
    }

    fun getExifRotation(file: File): ImageProcessingOptions.Orientation {
        val exif = ExifInterface(file.path)
        return when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> ImageProcessingOptions.Orientation.RIGHT_TOP
            ExifInterface.ORIENTATION_ROTATE_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            ExifInterface.ORIENTATION_ROTATE_270 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
            else -> ImageProcessingOptions.Orientation.TOP_LEFT
        }
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<Classifications>?
        )
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}
