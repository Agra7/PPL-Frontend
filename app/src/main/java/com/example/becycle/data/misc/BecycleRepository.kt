package com.example.becycle.data.misc

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.becycle.data.local.entity.ArticleEntity
import com.example.becycle.data.local.entity.HistoryEntity
import com.example.becycle.data.local.room.ArticleDao
import com.example.becycle.data.local.room.HistoryDao
import com.example.becycle.data.remote.response.ArticlesItem
import com.example.becycle.data.remote.response.HistoryResponse
import com.example.becycle.data.remote.response.RecyclePredictResponse
import com.example.becycle.data.remote.response.UserResponse
import com.example.becycle.data.remote.retrofit.ApiService
import okhttp3.MultipartBody

class BecycleRepository private constructor(
    private val apiService: ApiService,
    private val articleDao: ArticleDao,
    private val historyDao: HistoryDao
) {

    // Articles
    fun getArticles(token: String): LiveData<Result<List<ArticleEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response: List<ArticlesItem> = apiService.getArticles("Bearer $token")
            val articleEntities = response.map { item ->
                ArticleEntity(
                    // item.id is not used as local ID is auto-generated
                    source = item.source,
                    author = item.author,
                    title = item.title,
                    description = item.description,
                    url = item.url,
                    urlToImage = item.urlToImage,
                    publishedAt = item.publishedAt,
                    content = item.content
                )
            }
            // Insert/replace articles in local DB
            articleEntities.forEach { articleDao.insertArticle(it) }
            val localData: LiveData<Result<List<ArticleEntity>>> =
                articleDao.getAllArticles().map { Result.Success(it) }
            emitSource(localData)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllArticlesLocal(): LiveData<List<ArticleEntity>> = articleDao.getAllArticles()
    suspend fun insertArticle(article: ArticleEntity) = articleDao.insertArticle(article)
    suspend fun updateArticle(article: ArticleEntity) = articleDao.updateArticle(article)
    suspend fun deleteArticleById(id: Int) = articleDao.deleteArticleById(id)

    // History
    fun getHistory(token: String): LiveData<Result<List<HistoryEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response: List<HistoryResponse> = apiService.getHistory("Bearer $token")
            val historyEntities = response.map { item ->
                HistoryEntity(
                    historyId = item.historyId,
                    imageUrl = item.imageUrl,
                    predictionResult = item.predictionResult,
                    createdAt = item.createdAt,
                    userId = item.userId,
                    recycleId = item.recycleId
                )
            }
            // Insert/replace history in local DB
            historyEntities.forEach { historyDao.insertHistory(it) }
            val localData: LiveData<Result<List<HistoryEntity>>> =
                historyDao.getHistory().map { Result.Success(it) }
            emitSource(localData)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getHistoryLocal(): LiveData<List<HistoryEntity>> = historyDao.getHistory()
    suspend fun insertHistory(history: HistoryEntity) = historyDao.insertHistory(history)
    suspend fun updateHistory(history: HistoryEntity) = historyDao.updateHistory(history)
    suspend fun deleteHistoryById(historyId: Int) = historyDao.deleteHistoryById(historyId)

    // Remote-only APIs
    suspend fun getHistoryById(token: String, historyId: Int): HistoryResponse =
        apiService.getHistoryById("Bearer $token", historyId)

    suspend fun postRecyclePredict(token: String, image: MultipartBody.Part): RecyclePredictResponse =
        apiService.postRecyclePredict("Bearer $token", image)

    suspend fun getUserById(token: String, userId: Int): UserResponse =
        apiService.getUserById("Bearer $token", userId)

    companion object {
        @Volatile
        private var instance: BecycleRepository? = null
        fun getInstance(
            apiService: ApiService,
            articleDao: ArticleDao,
            historyDao: HistoryDao
        ): BecycleRepository =
            instance ?: synchronized(this) {
                instance ?: BecycleRepository(apiService, articleDao, historyDao)
            }.also { instance = it }
    }
}