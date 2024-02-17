package com.mikeschvedov.msapps_home_assignment.di

import android.content.Context
import androidx.room.Room
import com.mikeschvedov.msapps_home_assignment.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DBName = "app_database"

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, AppDatabase::class.java, DBName)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideFavoritesDao(appDB: AppDatabase) =
        appDB.favoritesDao()

}
