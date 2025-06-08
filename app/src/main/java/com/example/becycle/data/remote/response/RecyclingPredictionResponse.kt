package com.example.becycle.data.remote.response

import com.google.gson.annotations.SerializedName

data class RecyclingPredictionResponse(
    @SerializedName("recycle_id")
    val recycleId: Int,

    @SerializedName("material_type")
    val materialType: String,

    @SerializedName("can_be_recycled")
    val canBeRecycled: Boolean,

    @SerializedName("recycle_process")
    val recycleProcess: String,

    @SerializedName("possible_products")
    val possibleProducts: String,

    @SerializedName("image_url")
    val imageUrl: String
)