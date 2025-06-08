package com.example.becycle.data.misc.di

import android.content.Context
import com.example.becycle.data.local.room.DatabaseBecycle
import com.example.becycle.data.misc.BecycleRepository
import com.example.becycle.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): BecycleRepository {
        val apiService = ApiConfig.getApiService()

        val database = try {
            DatabaseBecycle.getInstance(context)
        } catch (e: Exception) {
            throw e
        }
        val historyDao = database.historyDao()
        val articleDao = database.articleDao()
        val recycleDao = database.recycleDao()
        val userDao = database.userDao()

        return BecycleRepository.getInstance(apiService,articleDao ,historyDao)
    }
}