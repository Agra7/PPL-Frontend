package com.example.becycle.model

data class HistoryResponse(
    val scan_id: Int,       // Matches your database column
    val user_id: Int,       // Matches your database column
    val image_url: String,  // URL of the scanned image
    val result: String,     // The analysis result (e.g., "plastic", "paper")
    val created_at: String  // Timestamp from the database
)