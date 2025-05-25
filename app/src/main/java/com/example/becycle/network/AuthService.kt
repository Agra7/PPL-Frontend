package com.example.becycle.network

import com.example.becycle.model.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class RegisterRequest(val username: String, val email: String, val password: String)
data class LoginRequest(val email: String, val password: String)

interface AuthService {
    @POST("/auth/register")
    fun register(@Body request: RegisterRequest): Call<Void>

    @POST("/auth/login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>
}
