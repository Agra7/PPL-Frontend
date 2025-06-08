package com.example.becycle.data.remote.response
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("user_id")
    val userId: Int,

    @field:SerializedName("full_name")
    val fullName: String,

    @field:SerializedName("bio")
    val bio: String,

    @field:SerializedName("address")
    val address: String,

    @field:SerializedName("exp")
    val exp: Int,

    @field:SerializedName("profile_picture")
    val profilePicture: String
)
