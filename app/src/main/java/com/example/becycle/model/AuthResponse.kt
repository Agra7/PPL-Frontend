package com.example.becycle.model

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user_id: String
)
