package com.example.becycle.data.remote.response
import com.google.gson.annotations.SerializedName

data class RecyclePredictResponse(
    @field:SerializedName("recycle_id")
    val recycleId: Int,

    @field:SerializedName("material_type")
    val materialType: String,

    @field:SerializedName("can_be_recycled")
    val canBeRecycled: Boolean,

    @field:SerializedName("recycle_process")
    val recycleProcess: String,

    @field:SerializedName("possible_products")
    val possibleProducts: String,

    @field:SerializedName("image_url")
    val imageUrl: String
)
