package com.example.becycle.data.remote.response
import com.google.gson.annotations.SerializedName
import com.example.becycle.items.Source

data class NewsResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("totalResults")
    val totalResults: Int,

    @field:SerializedName("articles")
    val articles: List<ArticlesItem>
)

data class ArticlesItem(
    @field:SerializedName("source")
    val source: Source,

    @field:SerializedName("author")
    val author: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("urlToImage")
    val urlToImage: String,

    @field:SerializedName("publishedAt")
    val publishedAt: String,

    @field:SerializedName("content")
    val content: String
)
