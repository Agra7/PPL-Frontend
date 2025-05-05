package com.example.becycle.activity

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.becycle.items.Article
import com.example.becycle.adapters.ArticleAdapter
import com.example.becycle.R

class ArticleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        setupBottomNavIfNeeded()

        val recyclerView = findViewById<RecyclerView>(R.id.articleRecyclerView)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager

        val articles = listOf(
            Article(1, "Short Title 1", "Website", "Short desc", "01-01-2025, 10:00", false),
            Article(2, "Long Title 2", "Website", "This is a long description with multiple lines...", "02-01-2025, 11:00", true),
            Article(3, "Short Title 3", "Website", "Short desc", "03-01-2025, 12:00", false),
            Article(4, "Long Title 4", "Website", "Another long article description here...", "04-01-2025, 13:00", true),
            Article(5, "Short Title 5", "Website", "Short desc", "05-01-2025, 14:00", false),
            Article(6, "Short Title 6", "Website", "Short desc", "06-01-2025, 15:00", false),
            Article(7, "Long Title 7", "Website", "This is a long description with many sentences...", "07-01-2025, 16:00", true),
            Article(8, "Short Title 8", "Website", "Short desc", "08-01-2025, 17:00", false),
            Article(9, "Long Title 9", "Website", "A very long article description goes here...", "09-01-2025, 18:00", true),
            Article(10, "Short Title 10", "Website", "Short desc", "10-01-2025, 19:00", false)
        )

        recyclerView.adapter = ArticleAdapter(articles)
    }
}