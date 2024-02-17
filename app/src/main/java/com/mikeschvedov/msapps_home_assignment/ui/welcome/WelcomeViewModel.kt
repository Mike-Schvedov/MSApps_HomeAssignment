package com.mikeschvedov.msapps_home_assignment.ui.welcome

import android.os.Debug
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikeschvedov.msapps_home_assignment.data.models.Article
import com.mikeschvedov.msapps_home_assignment.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    // LiveData to hold the list of articles
    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    // LiveData for error
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchArticlesByCategory(categoryName: String) {
        viewModelScope.launch {
            try {
                // Get Articles from the repository
                val articlesResult = repository.getArticlesByCategory(categoryName)

                // Retrieve favorite articles from the database
                val favoriteArticles = repository.getFavoriteArticles()

                // Update the LiveData with the fetched articles
                _articles.value = articlesResult.map { article ->
                    // Check if the current article is in the list of favorite articles
                    val isFavorite = favoriteArticles.any { it.title == article.title }
                    // Update the isFavorite property of the article
                    article.copy(isFavorite = isFavorite)
                }

            } catch (e: Exception) {
                // Handle error
                _error.value = "Error fetching articles: ${e.message}"
            }
        }
    }

    fun toggleArticleFavoriteStatus(article: Article) {
        viewModelScope.launch {
            if (article.isFavorite) {
                repository.addArticleToFavorites(article)
            } else {
                repository.removeArticleFromFavorites(article)
            }
        }
    }
}