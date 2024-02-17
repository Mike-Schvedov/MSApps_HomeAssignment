package com.mikeschvedov.msapps_home_assignment.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ArticleResponse(
    val pagination: Pagination,
    val data: List<Article>
)

data class Pagination(
    val limit: Int,
    val offset: Int,
    val count: Int,
    val total: Int
)

@Entity
data class Article(
    val author: String?,
    @PrimaryKey val title: String,
    val description: String?,
    val url: String,
    val source: String?,
    val image: String?,
    val category: String?,
    val language: String?,
    val country: String?,
    val published_at: String,
    var isFavorite: Boolean = false
)