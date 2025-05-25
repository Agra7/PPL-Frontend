package com.example.becycle.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.becycle.R
import com.example.becycle.adapters.HistoryAdapter
import com.example.becycle.items.HistoryItem
import com.example.becycle.model.HistoryResponse
import com.example.becycle.network.ApiClient
import com.example.becycle.network.HistoryService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : BaseActivity() {

    private lateinit var tabScanned: TextView
    private lateinit var tabCreations: TextView
    private lateinit var recyclerView: RecyclerView

    private val historyService = ApiClient.retrofit.create(HistoryService::class.java)
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Get user ID from SharedPreferences as String
        val sharedPref = getSharedPreferences("auth", MODE_PRIVATE)
        userId = sharedPref.getString("user_id", null)

        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupBottomNavIfNeeded()

        tabScanned = findViewById(R.id.tab_scanned)
        tabCreations = findViewById(R.id.tab_recycling)
        recyclerView = findViewById(R.id.history_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        tabScanned.setOnClickListener {
            highlightTab(tabScanned, tabCreations)
            fetchHistory()
        }

        tabCreations.setOnClickListener {
            highlightTab(tabCreations, tabScanned)
            showUserCreations()
        }

        fetchHistory() // Load history by default
    }

    private fun highlightTab(selected: TextView, other: TextView) {
        selected.setBackgroundResource(R.drawable.tab_selected)
        selected.setTextColor(ContextCompat.getColor(this, R.color.green))
        other.setBackgroundResource(android.R.color.transparent)
        other.setTextColor(ContextCompat.getColor(this, R.color.gray))
    }

    private fun fetchHistory() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val history = historyService.getHistory(userId!!)
                withContext(Dispatchers.Main) {
                    updateHistoryList(history)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@HistoryActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateHistoryList(history: List<HistoryResponse>) {
        val historyItems = history.map {
            HistoryItem(
                title = it.result,
                date = it.created_at,
                imageUrl = it.image_url
            )
        }
        recyclerView.adapter = HistoryAdapter(historyItems)
    }

    private fun showUserCreations() {
        val dummyCreations = listOf(
            HistoryItem("Plastic Planter", "Uploaded Apr 12, 2025", "https://example.com/planter.jpg"),
            HistoryItem("Bottle Lamp", "Uploaded Mar 28, 2025", "https://example.com/lamp.jpg")
        )
        recyclerView.adapter = HistoryAdapter(dummyCreations)
    }
}