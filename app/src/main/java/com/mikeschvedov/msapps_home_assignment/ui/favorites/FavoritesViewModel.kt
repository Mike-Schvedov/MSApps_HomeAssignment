package com.mikeschvedov.msapps_home_assignment.ui.favorites

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
class FavoritesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    // LiveData to hold the list of articles
    private val _favorites = MutableLiveData<List<Article>>()
    val favorites: LiveData<List<Article>> = _favorites

    // LiveData for error
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchAllFavorites() {
        viewModelScope.launch {
            try {
                // Get Favorites from the repository
                val favorites = repository.getFavoriteArticles()
                println("feched this amount of favorites: " +favorites.size)
                // Update the LiveData with the fetched articles
                _favorites.value = favorites
            } catch (e: Exception) {
                // Handle error
                _error.value = "Error fetching articles: ${e.message}"
            }
        }
    }

    fun removeArticleFromFavorites(article: Article) {
        viewModelScope.launch {
            repository.removeArticleFromFavorites(article)

        }
    }

}
