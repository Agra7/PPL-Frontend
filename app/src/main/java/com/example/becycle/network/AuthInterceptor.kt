// com/example/becycle/network/AuthInterceptor.kt
package com.example.becycle.network

import com.example.becycle.utils.UserPreference
import okhttp3.Interceptor
import okhttp3.Response
import android.content.Context // You need context to create UserPreference

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val userPreference = UserPreference(context) // Create UserPreference instance
        val accessToken = userPreference.getAccessToken()

        val requestBuilder = originalRequest.newBuilder()

        if (accessToken != null) {
            // Add Authorization header with Bearer token
            requestBuilder.header("Authorization", "Bearer $accessToken")
        }

        // Proceed with the request
        return chain.proceed(requestBuilder.build())
    }
}