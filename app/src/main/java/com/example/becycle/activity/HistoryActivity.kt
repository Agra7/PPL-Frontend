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

class HistoryActivity : BaseActivity() {

    private lateinit var tabScanned: TextView
    private lateinit var tabCreations: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter

    private var userId: String? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: MainViewModel by viewModels { factory }

        tabScanned = findViewById(R.id.tab_scanned)
        tabCreations = findViewById(R.id.tab_recycling)
        recyclerView = findViewById(R.id.history_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        historyAdapter = HistoryAdapter(emptyList())
        recyclerView.adapter = historyAdapter
        Log.e("HistoryActivity111111", "Setting adapter: $historyAdapter")
        fetchHistory(viewModel)
        Log.e("checkpoint", "babibubebo")
        // Get user ID and token from SharedPreferences
//        val sharedPref = getSharedPreferences("auth", MODE_PRIVATE)
//        userId = sharedPref.getString("user_id", null)
//        token = sharedPref.getString("token", null)

//        if (userId.isNullOrEmpty() || token.isNullOrEmpty()) {
//            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
//            return
//        }

//        setupBottomNavIfNeeded()


//        tabScanned.setOnClickListener {
//            highlightTab(tabScanned, tabCreations)
//            fetchHistory(viewModel)
//        }
//
//        tabCreations.setOnClickListener {
//            highlightTab(tabCreations, tabScanned)
//            showUserCreations()
//        }

         // Load history by default
    }

    private fun highlightTab(selected: TextView, other: TextView) {
        selected.setBackgroundResource(R.drawable.tab_selected)
        selected.setTextColor(ContextCompat.getColor(this, R.color.green))
        other.setBackgroundResource(android.R.color.transparent)
        other.setTextColor(ContextCompat.getColor(this, R.color.gray))
    }

    private fun fetchHistory(viewModel: MainViewModel) {
        viewModel.getHistory("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjozLCJlbWFpbCI6ImFsdWNhcmRrYXJpbmE2MzVAZ21haWwuY29tIiwiaXNWZXJpZmllZCI6dHJ1ZSwiaWF0IjoxNzQ5MDk0MzMyLCJleHAiOjE3NDkwOTc5MzJ9.hyaFqqMcEB-YLczdbwlcp1ut_hBI7hmkm5sn3imxIns").observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    Log.e("HistoryActivity111111", "Result.Success, data count: ${result.data.size}")
                    historyAdapter.updateHistory(result.data)
                    if (result.data.isEmpty()) {
                        Log.e("HistoryActivity111111", "Result.Success but list is empty")
                    }
                }
                is Result.Loading -> {
                    Log.e("HistoryActivity111111", "Result.Loading")
                }
                is Result.Error -> {
                    Log.e("HistoryActivity111111", "Result.Error: ${result.error}")
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showUserCreations() {
        historyAdapter.updateHistory(emptyList())
    }
}