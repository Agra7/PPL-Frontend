package com.example.becycle.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.asclepius.models.ViewModelFactory
import com.example.becycle.adapters.ArticleAdapter
import com.example.becycle.R
import com.example.becycle.model.models.MainViewModel
import com.example.becycle.data.misc.Result

class ArticleActivity : BaseActivity() {

    private var userId: String? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        setupBottomNavIfNeeded()

        val recyclerView = findViewById<RecyclerView>(R.id.articleRecyclerView)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager

        // Prepare the adapter with an empty list
        val articleAdapter = ArticleAdapter(emptyList())
        recyclerView.adapter = articleAdapter

        val sharedPref = getSharedPreferences("auth", MODE_PRIVATE)
        userId = sharedPref.getString("user_id", null)
        token = sharedPref.getString("token", null)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: MainViewModel by viewModels { factory }

        fetchArticle(viewModel, articleAdapter, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjozLCJlbWFpbCI6ImFsdWNhcmRrYXJpbmE2MzVAZ21haWwuY29tIiwiaXNWZXJpZmllZCI6dHJ1ZSwiaWF0IjoxNzQ5MDk0MzMyLCJleHAiOjE3NDkwOTc5MzJ9.hyaFqqMcEB-YLczdbwlcp1ut_hBI7hmkm5sn3imxIns")
    }

    private fun fetchArticle(viewModel: MainViewModel, articleAdapter: ArticleAdapter, token: String) {
        viewModel.getArticles(token).observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    Log.d("ArticleActivity", "articles successfully.")
                    val articles = result.data
                    articleAdapter.updateArticles(articles)
                }
                is Result.Error -> {
                    // Handle error (show a message, etc)
                    Log.e("ArticleActivity", "articles failed: ${result.error}")
                }
                is Result.Loading -> {
                    // Show loading if needed
                }
                // Optionally handle other cases if you have them
            }
        }
    }
}