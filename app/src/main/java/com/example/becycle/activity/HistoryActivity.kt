package com.example.becycle.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.models.ViewModelFactory
import com.example.becycle.R
import com.example.becycle.adapters.HistoryAdapter
import com.example.becycle.data.local.entity.HistoryEntity
import com.example.becycle.data.misc.Result
import com.example.becycle.model.models.MainViewModel
import com.example.becycle.utils.UserPreference // Import UserPreference

class HistoryActivity : BaseActivity() {

    private lateinit var tabScanned: TextView
    private lateinit var tabCreations: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter

    // You might not need these as class members if you get them directly when needed
    // private var userId: String? = null
    // private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Initialize UserPreference here
        userPreference = UserPreference(this)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: MainViewModel by viewModels { factory }

        tabScanned = findViewById(R.id.tab_scanned)
        tabCreations = findViewById(R.id.tab_recycling)
        recyclerView = findViewById(R.id.history_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        historyAdapter = HistoryAdapter(emptyList())
        recyclerView.adapter = historyAdapter
        Log.e("HistoryActivity", "Setting adapter: $historyAdapter")

        // Retrieve token from UserPreference
        val token = userPreference.getAccessToken()
        val userId = userPreference.getUserId() // You might need userId for other specific API calls

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "User not logged in or session expired. Please log in again.", Toast.LENGTH_LONG).show()
            // Optionally, redirect to login activity here
            // val intent = Intent(this, MainActivity::class.java)
            // startActivity(intent)
            finish() // Close HistoryActivity if no token
            return
        }

        fetchHistory(viewModel, token) // Pass the retrieved token
        Log.e("checkpoint", "babibubebo")

        tabScanned.setOnClickListener {
            highlightTab(tabScanned, tabCreations)
            fetchHistory(viewModel, token) // Pass the token here too
        }

        tabCreations.setOnClickListener {
            highlightTab(tabCreations, tabScanned)
            // If showUserCreations needs a token, pass it. Otherwise, it's fine.
            showUserCreations()
        }

        highlightTab(tabScanned, tabCreations) // Load history by default and highlight scanned tab
    }

    private fun highlightTab(selected: TextView, other: TextView) {
        selected.setBackgroundResource(R.drawable.tab_selected)
        selected.setTextColor(ContextCompat.getColor(this, R.color.green))
        other.setBackgroundResource(android.R.color.transparent)
        other.setTextColor(ContextCompat.getColor(this, R.color.gray))
    }

    // Modify fetchHistory to accept the token
    private fun fetchHistory(viewModel: MainViewModel, token: String) {
        // Use the token parameter here, not a hardcoded string
        viewModel.getHistory(token).observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    Log.e("HistoryActivity", "Result.Success, data count: ${result.data.size}")
                    historyAdapter.updateHistory(result.data)
                    if (result.data.isEmpty()) {
                        Log.e("HistoryActivity", "Result.Success but list is empty")
                        // Optional: Show a "No history found" message
                    }
                }
                is Result.Loading -> {
                    Log.e("HistoryActivity", "Result.Loading")
                    // Optional: Show a loading spinner
                }
                is Result.Error -> {
                    Log.e("HistoryActivity", "Result.Error: ${result.error}")
                    Toast.makeText(this, "Error fetching history: ${result.error}", Toast.LENGTH_SHORT).show()
                    // Optional: Hide loading spinner
                }
            }
        }
    }

    private fun showUserCreations() {
        // This function doesn't currently make an API call, so it might not need the token directly.
        // If "Creations" data comes from an API, you'd fetch it here, likely also needing the token.
        historyAdapter.updateHistory(emptyList()) // Assuming this just clears the list for now
    }
}