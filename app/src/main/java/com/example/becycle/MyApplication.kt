// com/example/becycle/MyApplication.kt
package com.example.becycle

import android.app.Application
import com.example.becycle.data.misc.BecycleRepository
import com.example.becycle.data.local.room.DatabaseBecycle
import com.example.becycle.data.remote.retrofit.ApiService
import com.example.becycle.network.ApiClient // Ensure this import is correct
import androidx.appcompat.app.AppCompatDelegate

class MyApplication : Application() {

    // Instance repository yang bisa diakses global
    lateinit var becycleRepository: BecycleRepository
        private set

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // 1. Initialize ApiClient FIRST, before any API service creation
        // This ensures the OkHttpClient with AuthInterceptor is set up.
        ApiClient.initialize(this) // Pass 'this' (the Application context)

        // Now you can safely create your ApiService, as ApiClient.retrofit is initialized
        val database = DatabaseBecycle.getInstance(this)
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        // Then initialize your repository
        // Make sure BecycleRepository.getInstance accepts ApiService and DAO as parameters
        // Or if you use DI, it would be provided differently
        // becycleRepository = BecycleRepository.getInstance(apiService, database.historyDao())
    }
}