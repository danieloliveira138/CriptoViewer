package com.danieloliveira138.criptoviewer.data.repository

import com.danieloliveira138.criptoviewer.data.remote.APIService
import dagger.Module
import jakarta.inject.Inject

@Module
class CryptoRepository @Inject constructor(
    private val apiService: APIService
) {
    suspend fun getCoins() = apiService.getCoins()
}