package com.example.becycle.model.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.becycle.data.local.entity.ArticleEntity
import com.example.becycle.data.local.entity.HistoryEntity
import com.example.becycle.data.misc.BecycleRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class MainViewModel(private val beCycleRepository: BecycleRepository) : ViewModel() {

    // ARTICLES
    fun getArticles(token: String) = beCycleRepository.getArticles(token)
    fun getAllArticlesLocal() = beCycleRepository.getAllArticlesLocal()

    fun insertArticle(articleEntity: ArticleEntity) {
        viewModelScope.launch {
            beCycleRepository.insertArticle(articleEntity)
        }
    }
    fun updateArticle(articleEntity: ArticleEntity) {
        viewModelScope.launch {
            beCycleRepository.updateArticle(articleEntity)
        }
    }
    fun deleteArticleById(id: Int) {
        viewModelScope.launch {
            beCycleRepository.deleteArticleById(id)
        }
    }

    // HISTORY
    fun getHistory(token: String) = beCycleRepository.getHistory(token)
    fun getHistoryLocal() = beCycleRepository.getHistoryLocal()

    fun insertHistory(historyEntity: HistoryEntity) {
        viewModelScope.launch {
            beCycleRepository.insertHistory(historyEntity)
        }
    }
    fun updateHistory(historyEntity: HistoryEntity) {
        viewModelScope.launch {
            beCycleRepository.updateHistory(historyEntity)
        }
    }
    fun deleteHistoryById(historyId: Int) {
        viewModelScope.launch {
            beCycleRepository.deleteHistoryById(historyId)
        }
    }

    // REMOTE DETAIL, PREDICTION AND USER
    suspend fun getHistoryById(token: String, historyId: Int) =
        beCycleRepository.getHistoryById(token, historyId)

    suspend fun postRecyclePredict(token: String, image: MultipartBody.Part) =
        beCycleRepository.postRecyclePredict(token, image)

    suspend fun getUserById(token: String, userId: Int) =
        beCycleRepository.getUserById(token, userId)
}