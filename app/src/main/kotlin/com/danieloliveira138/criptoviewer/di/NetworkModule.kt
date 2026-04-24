package com.danieloliveira138.criptoviewer.di

import com.danieloliveira138.criptoviewer.BuildConfig
import com.danieloliveira138.criptoviewer.data.remote.api.ExchangeAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-CMC_PRO_API_KEY", BuildConfig.CMC_API_KEY)
                    .addHeader("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): ExchangeAPI {
        val contentType = "application/json".toMediaType()
        val jsonConverter =
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                coerceInputValues = true
            }

        return Retrofit.Builder()
            .baseUrl("https://pro-api.coinmarketcap.com/")
            .client(okHttpClient)
            .addConverterFactory(jsonConverter.asConverterFactory(contentType))
            .build()
            .create(ExchangeAPI::class.java)
    }
}