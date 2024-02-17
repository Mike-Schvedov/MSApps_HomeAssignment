package com.mikeschvedov.msapps_home_assignment.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mikeschvedov.msapps_home_assignment.data.database.dao.FavoriteArticleDao
import com.mikeschvedov.msapps_home_assignment.data.models.Article

@Database(entities = [Article::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun favoritesDao(): FavoriteArticleDao
}