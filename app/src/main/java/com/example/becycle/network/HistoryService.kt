package com.example.becycle.network

import com.example.becycle.model.HistoryRequest
import com.example.becycle.model.HistoryResponse
import com.example.becycle.model.ImageUploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface HistoryService {
    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<ImageUploadResponse>

    @POST("history")
    suspend fun saveHistoryItem(
        @Body request: HistoryRequest
    ): Response<HistoryResponse>

    @GET("history")
    suspend fun getHistory(
        @Query("user_id") userId: String
    ): List<HistoryResponse>
}