package com.example.becycle.items

data class Article(
    val id: Int,
    val title: String,
    val website: String,
    val description: String,
    val dateTime: String,
    val isLongArticle: Boolean
)