package com.mikeschvedov.msapps_home_assignment.data.network

import com.mikeschvedov.msapps_home_assignment.data.models.ArticleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v1/news")
    suspend fun getArticlesByCategory(
        @Query("categories") category: String
    ): ArticleResponse

}
