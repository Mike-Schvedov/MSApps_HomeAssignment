package com.mikeschvedov.msapps_home_assignment.data.repository

import com.mikeschvedov.msapps_home_assignment.data.models.Article

interface Repository {
    suspend fun getArticlesByCategory(categoryName: String): List<Article>
    suspend fun addArticleToFavorites(article: Article)
    suspend fun removeArticleFromFavorites(article: Article)
    suspend fun getFavoriteArticles(): List<Article>
}