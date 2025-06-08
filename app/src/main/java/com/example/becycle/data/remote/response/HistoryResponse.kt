package com.example.becycle.data.remote.response
import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @field:SerializedName("history_id")
    val historyId: Int,

    @field:SerializedName("image_url")
    val imageUrl: String,

    @field:SerializedName("prediction_result")
    val predictionResult: String,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("user_id")
    val userId: Int,

    @field:SerializedName("recycle_id")
    val recycleId: Int
)
