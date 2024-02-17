package com.mikeschvedov.msapps_home_assignment.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikeschvedov.msapps_home_assignment.data.models.Article

@Dao
interface FavoriteArticleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticle(article: Article)

    @Query("DELETE FROM article WHERE title = :title")
    suspend fun deleteArticle(title: String)

    @Query("SELECT * FROM article")
    suspend fun getFavoriteArticles(): List<Article>

}