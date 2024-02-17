package com.mikeschvedov.msapps_home_assignment.di

import com.mikeschvedov.msapps_home_assignment.data.network.ApiService
import com.mikeschvedov.msapps_home_assignment.apikey.ApiKeyManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val NEWS_API_BASE_URL = "http://api.mediastack.com/"
    private const val API_KEY_NAME = "access_key"
    private const val API_KEY_VALUE = ApiKeyManager.API_KEY

    //  ------------ Retrofit ------------ //
    @Provides
    fun provideRetrofit(
        httpClient: OkHttpClient,
        gsonConverterFactory: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(NEWS_API_BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .build()

    //  ------------ Authorization Interceptor (So we don't have to specify the API KEY in each request)------------ //
    @Provides
    fun provideAuthorizationInterceptor() = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            // Starting the request chain
            val originalRequest = chain.request()

            // If we don't have the api key return the original request
            if (API_KEY_VALUE.isBlank()) return chain.proceed(originalRequest)

            // Passing the key as a query to all requests
            val url = originalRequest.url().newBuilder()
                .addQueryParameter(API_KEY_NAME, API_KEY_VALUE)
                .build()
            val newRequest = originalRequest.newBuilder().url(url).build()
            return chain.proceed(newRequest)
        }
    }


    //  ------------ OkHttp ------------ //
    @Provides
    fun provideOKHTTPClient(
        authInterceptor: Interceptor,
    ): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(authInterceptor)
            .build()

    //  ------------ NewsApi ------------ //
    @Provides
    fun provideNewsApi(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    //  ------------ GsonFactory ------------ //
    @Provides
    fun provideGsonFactory(): Converter.Factory = GsonConverterFactory.create()
}