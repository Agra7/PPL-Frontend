// com/example/becycle/network/ApiClient.kt
package com.example.becycle.network

import android.content.Context // Import Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://project-ppl-production.up.railway.app"

    // Retrofit instance, now with an OkHttpClient
    private lateinit var _retrofit: Retrofit // Use lateinit to initialize later

    // Initialize ApiClient from Application or first Activity
    fun initialize(context: Context) {
        if (!::_retrofit.isInitialized) {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context)) // Add the AuthInterceptor
                .build()

            _retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient) // Set the OkHttpClient
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    // Public getter for the Retrofit instance
    val retrofit: Retrofit
        get() {
            if (!::_retrofit.isInitialized) {
                // This block should ideally not be reached if initialize() is called early
                // You might throw an error or log a warning if it is.
                throw IllegalStateException("ApiClient not initialized. Call initialize(context) first.")
            }
            return _retrofit
        }
}