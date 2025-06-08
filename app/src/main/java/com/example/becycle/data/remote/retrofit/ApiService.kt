package com.example.becycle.data.remote.retrofit

import com.example.becycle.data.remote.response.ArticlesItem
import com.example.becycle.data.remote.response.HistoryResponse
import com.example.becycle.data.remote.response.RecyclePredictResponse
import com.example.becycle.data.remote.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface ApiService {

    // GET /articles
    @GET("articles")
    suspend fun getArticles(
        @Header("Authorization") token: String
    ): List<ArticlesItem>

    // GET /history
    @GET("history")
    suspend fun getHistory(
        @Header("Authorization") token: String
    ): List<HistoryResponse>

    // GET /history/:history_id
    @GET("history/{history_id}")
    suspend fun getHistoryById(
        @Header("Authorization") token: String,
        @Path("history_id") historyId: Int
    ): HistoryResponse

    // POST /recycling-predict
    @Multipart
    @POST("recycling-predict")
    suspend fun postRecyclePredict(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): RecyclePredictResponse

    // GET /users/:user_id
    @GET("users/{user_id}")
    suspend fun getUserById(
        @Header("Authorization") token: String,
        @Path("user_id") userId: Int
    ): UserResponse
}