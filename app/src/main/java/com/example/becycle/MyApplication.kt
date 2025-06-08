package com.example.becycle

import android.app.Application
import com.example.becycle.data.misc.BecycleRepository
import com.example.becycle.data.local.room.DatabaseBecycle
import com.example.becycle.data.remote.retrofit.ApiService
import com.example.becycle.network.ApiClient

class MyApplication : Application() {

    // Instance repository yang bisa diakses global
    lateinit var becycleRepository: BecycleRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = DatabaseBecycle.getInstance(this)
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
//        becycleRepository = BecycleRepository.getInstance(apiService, database.historyDao())
    }
}
