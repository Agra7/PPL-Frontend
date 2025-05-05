package com.example.becycle.activity

import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.becycle.adapters.HistoryAdapter
import com.example.becycle.items.HistoryItem
import com.example.becycle.R

class HistoryActivity : BaseActivity() {

    private lateinit var tabScanned: TextView
    private lateinit var tabCreations: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setupBottomNavIfNeeded()

        tabScanned = findViewById(R.id.tab_scanned)
        tabCreations = findViewById(R.id.tab_recycling)
        recyclerView = findViewById(R.id.history_recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(this)

        showScannedItems() // default

        tabScanned.setOnClickListener {
            highlightTab(tabScanned, tabCreations)
            showScannedItems()
        }

        tabCreations.setOnClickListener {
            highlightTab(tabCreations, tabScanned)
            showUserCreations()
        }
    }

    private fun highlightTab(selected: TextView, other: TextView) {
        selected.setBackgroundResource(R.drawable.tab_selected)
        selected.setTextColor(ContextCompat.getColor(this, R.color.green))
        other.setBackgroundResource(android.R.color.transparent)
        other.setTextColor(ContextCompat.getColor(this, R.color.gray))
    }

    private fun showScannedItems() {
        val scannedList = listOf(
            HistoryItem("Plastic Bottle", "April 20, 2025", R.drawable.plant_placeholder),
            HistoryItem("Glass Jar", "April 19, 2025", R.drawable.plant_placeholder),
            HistoryItem("Can", "April 17, 2025", R.drawable.plant_placeholder)
        )
        recyclerView.adapter = HistoryAdapter(scannedList)
    }

    private fun showUserCreations() {
        val creationList = listOf(
            HistoryItem("Pot Planter", "Uploaded April 10, 2025", R.drawable.plant_placeholder),
            HistoryItem("Bird Feeder", "Uploaded April 5, 2025", R.drawable.plant_placeholder)
        )
        recyclerView.adapter = HistoryAdapter(creationList)
    }

}
