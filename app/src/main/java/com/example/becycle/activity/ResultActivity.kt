package com.example.becycle.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.becycle.R
import com.example.becycle.adapters.CenterAdapter
import com.example.becycle.adapters.IdeaAdapter
import com.example.becycle.items.Idea
import com.example.becycle.model.HistoryRequest
import com.example.becycle.network.ApiClient
import com.example.becycle.network.HistoryService
import com.example.becycle.utils.UserPreference // Import UserPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.InputStream
import java.text.NumberFormat
import java.util.Locale

class ResultActivity : BaseActivity() {

    private lateinit var resultImage: ImageView
    private lateinit var labelText: TextView
//    private lateinit var scoreText: TextView
    private lateinit var ideaList: RecyclerView
    private lateinit var tabIdea: TextView
    private lateinit var tabCenter: TextView
    private lateinit var loadingOverlay: View

    // userId will be retrieved from UserPreference in onCreate
    // private var userId: Int = -1 // No longer needed as a class member with -1 default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Initialize UserPreference
        userPreference = UserPreference(this)

        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        val label = intent.getStringExtra(EXTRA_LABEL) ?: "Unknown"
//        val score = intent.getFloatExtra(EXTRA_SCORE, 0f)

        // Initialize UI components
        resultImage = findViewById(R.id.result_image_view)
        labelText = findViewById(R.id.detected_label)
//        scoreText = findViewById(R.id.detected_score) // Make sure you have this ID in your XML!
        ideaList = findViewById(R.id.idea_list)
        tabIdea = findViewById(R.id.tab_idea)
        tabCenter = findViewById(R.id.tab_center)
        loadingOverlay = findViewById(R.id.loading_overlay)

        setupBottomNavIfNeeded()

        // Get user ID and token from UserPreference
        val currentUserId = userPreference.getUserId()?.toIntOrNull()
        val accessToken = userPreference.getAccessToken()

        // Display the results
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            Glide.with(this)
                .load(imageUri)
                .into(resultImage)

            // Save to history automatically ONLY if user is logged in
            if (currentUserId != null && accessToken != null) {
                saveToHistory(label, imageUri, currentUserId, accessToken)
            } else {
                Log.w("ResultActivity", "User not logged in, skipping history save.")
                Toast.makeText(this, "Not logged in. History not saved.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No image received.", Toast.LENGTH_SHORT).show()
        }

        labelText.text = label
//        scoreText.text = NumberFormat.getPercentInstance(Locale.US).format(score) // Use the correct TextView

        // Setup RecyclerView and Tabs
        ideaList.layoutManager = LinearLayoutManager(this)
        highlightTab(selected = tabIdea, other = tabCenter) // Highlight default tab
        showIdeas() // Default to ideas tab

        tabIdea.setOnClickListener {
            highlightTab(selected = tabIdea, other = tabCenter)
            showIdeas()
        }

        tabCenter.setOnClickListener {
            highlightTab(selected = tabCenter, other = tabIdea)
            showCenters()
        }

//        findViewById<ImageView>(R.id.back_button).setOnClickListener { // Assuming you have a back_button
//            finish()
//        }
    }

    private fun highlightTab(selected: TextView, other: TextView) {
        selected.setBackgroundResource(R.drawable.tab_selected)
        selected.setTextColor(ContextCompat.getColor(this, R.color.green))

        other.setBackgroundResource(android.R.color.transparent)
        other.setTextColor(ContextCompat.getColor(this, R.color.gray))
    }

    // Pass userId and accessToken as parameters
    private fun saveToHistory(result: String, imageUri: Uri, userId: Int, accessToken: String) {
        loadingOverlay.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // First, upload the image to get a URL
                val imageUrl = uploadImage(imageUri, accessToken) // Pass token to uploadImage

                // Then, save the history item with the returned URL
                val historyRequest = HistoryRequest(
                    user_id = userId,
                    image_url = imageUrl,
                    result = result
                )

                // You might need to add an interceptor to ApiClient or explicitly pass the token here
                // For simplicity, let's assume the API client handles auth headers if needed
                // If not, you might need to manually add it to the request here or within the service.
                val historyService = ApiClient.retrofit.create(HistoryService::class.java)
                val response = historyService.saveHistoryItem(historyRequest) // The interceptor should add the auth header

                withContext(Dispatchers.Main) {
                    loadingOverlay.visibility = View.GONE
                    if (response.isSuccessful) {
                        Toast.makeText(this@ResultActivity, "Saved to history", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("ResultActivity", "Failed to save history: ${response.errorBody()?.string()}")
                        Toast.makeText(this@ResultActivity, "Failed to save to history. Error: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    loadingOverlay.visibility = View.GONE
                    Log.e("ResultActivity", "Exception in saveToHistory: ${e.message}")
                    Toast.makeText(this@ResultActivity, "Error saving history: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Pass accessToken as a parameter for authenticated upload
    private suspend fun uploadImage(uri: Uri, accessToken: String): String {
        return withContext(Dispatchers.IO) {
            val tempFile = File.createTempFile("upload_", ".jpg", cacheDir)
            contentResolver.openInputStream(uri)?.use { inputStream ->
                tempFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", tempFile.name, requestFile)

            // You might need to add an interceptor to ApiClient or manually add the header here
            // If using an interceptor, you don't need to pass accessToken here.
            // But if not, you'd modify HistoryService or pass a header manually:
            val historyService = ApiClient.retrofit.create(HistoryService::class.java)
            val response = historyService.uploadImage(body) // Assuming ApiClient's interceptor handles auth.
            // If not, you'd do: val response = historyService.uploadImage("Bearer $accessToken", body)

            tempFile.delete() // Clean up the temporary file

            if (response.isSuccessful) {
                response.body()?.imageUrl ?: throw Exception("Image URL is null")
            } else {
                throw Exception("Image upload failed: ${response.message()} (Code: ${response.code()})")
            }
        }
    }

    private fun showIdeas() {
        // ... (rest of showIdeas remains the same)
        val ideas = listOf(
            Idea(
                title = "Pot Planter",
                description = "Turn a plastic bottle into a decorative plant pot. It's perfect for herbs or succulents.",
                materials = "• Plastic bottle\n• Scissors\n• Paint\n• Soil\n• Seeds or plant",
                steps = "1. Cut the bottle in half\n2. Decorate it with paint\n3. Fill with soil\n4. Add plant\n5. Place on windowsill",
                references = "https://example.com/pot-planter\nhttps://youtube.com/example1"
            ),
            Idea(
                title = "Bird Feeder",
                description = "Create a simple bird feeder to hang in your backyard.",
                materials = "• Plastic bottle\n• Wooden spoons\n• String\n• Birdseed",
                steps = "1. Cut small holes for spoons\n2. Fill with seed\n3. Hang on tree\n4. Watch birds enjoy",
                references = "https://example.com/bird-feeder"
            )
        )
        ideaList.adapter = IdeaAdapter(ideas)
    }

    private fun showCenters() {
        // ... (rest of showCenters remains the same)
        val centers = listOf(
            "Pusat Daur Ulang Bandung (3.56 Km Away)\nJl. Buah Batu no.67 Kec. Gede Bage, 157368",
            "Bank Sampah Bersinar (4.1 Km Away)\nJl. Terusan Buah Batu No.21",
            "Pusat Daur Ulang Cimahi (15 Km Away)\nJl. Jend. H. Amir Machmud No.567",
        )
        ideaList.adapter = CenterAdapter(centers)
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_LABEL = "extra_label"
//        const val EXTRA_SCORE = "extra_score"
    }
}