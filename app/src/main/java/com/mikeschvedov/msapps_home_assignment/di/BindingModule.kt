package com.mikeschvedov.msapps_home_assignment.di

import com.mikeschvedov.msapps_home_assignment.data.repository.Repository
import com.mikeschvedov.msapps_home_assignment.data.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindingModule {

    @Binds
    @Singleton
    abstract fun bindRepository(repository: RepositoryImpl): Repository

}
