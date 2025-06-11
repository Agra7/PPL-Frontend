package com.example.becycle.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast // Import Toast for error messages if needed
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.asclepius.models.ViewModelFactory
import com.example.becycle.adapters.ArticleAdapter
import com.example.becycle.R
import com.example.becycle.model.models.MainViewModel
import com.example.becycle.data.misc.Result
import com.example.becycle.utils.UserPreference // Import UserPreference

class ArticleActivity : BaseActivity() {

    // You can remove these as class members if you only need them locally in onCreate
    // private var userId: String? = null
    // private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        // Initialize UserPreference
        userPreference = UserPreference(this)

        setupBottomNavIfNeeded()

        val recyclerView = findViewById<RecyclerView>(R.id.articleRecyclerView)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager

        // Prepare the adapter with an empty list
        val articleAdapter = ArticleAdapter(emptyList())
        recyclerView.adapter = articleAdapter

        // Retrieve token from UserPreference
        val token = userPreference.getAccessToken()
        val userId = userPreference.getUserId() // Retrieve userId if you need it elsewhere in this activity

        // Check if token exists before trying to fetch articles
        if (token.isNullOrEmpty()) {
            Log.w("ArticleActivity", "No authentication token found. Cannot fetch articles.")
            Toast.makeText(this, "Not logged in or session expired. Articles might not load.", Toast.LENGTH_LONG).show()
            // Optionally, you might want to redirect to login or show a limited view
            // return
        }

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: MainViewModel by viewModels { factory }

        // Pass the actual token retrieved from UserPreference
        // If token is null, pass null or handle it in your ViewModel/Repository
        fetchArticle(viewModel, articleAdapter, token)
    }

    // Change token parameter to be nullable if the API can handle it, or handle null token
    private fun fetchArticle(viewModel: MainViewModel, articleAdapter: ArticleAdapter, token: String?) {
        if (token == null) {
            Log.e("ArticleActivity", "Cannot fetch articles: Authentication token is missing.")
            // You might want to show a specific message to the user or change UI state
            return
        }

        viewModel.getArticles(token).observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    Log.d("ArticleActivity", "Articles fetched successfully. Count: ${result.data.size}")
                    val articles = result.data
                    articleAdapter.updateArticles(articles)
                    if (articles.isEmpty()) {
                        Log.d("ArticleActivity", "No articles found.")
                        // Optional: Show a "No articles available" message
                    }
                }
                is Result.Error -> {
                    Log.e("ArticleActivity", "Article fetch failed: ${result.error}")
                    Toast.makeText(this, "Failed to load articles: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    Log.d("ArticleActivity", "Loading articles...")
                    // Show loading indicator
                }
            }
        }
    }
}