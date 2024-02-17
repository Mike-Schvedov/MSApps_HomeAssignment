package com.mikeschvedov.msapps_home_assignment.data.repository

import com.mikeschvedov.msapps_home_assignment.data.database.dao.FavoriteArticleDao
import com.mikeschvedov.msapps_home_assignment.data.models.Article
import com.mikeschvedov.msapps_home_assignment.data.network.ApiService
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val favoriteArticleDao: FavoriteArticleDao
) : Repository {
    override suspend fun getArticlesByCategory(categoryName: String): List<Article> {
        return apiService.getArticlesByCategory(categoryName).data
    }

    override suspend fun addArticleToFavorites(article: Article) {
        println("Adding to favorites - repository")
        favoriteArticleDao.insertArticle(article)
    }

    override suspend fun removeArticleFromFavorites(article: Article) {
        favoriteArticleDao.deleteArticle(article.title)
    }

    override suspend fun getFavoriteArticles(): List<Article> {
        return favoriteArticleDao.getFavoriteArticles()
    }
}