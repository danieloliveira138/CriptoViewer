package com.danieloliveira138.criptoviewer.data.repository

import com.danieloliveira138.criptoviewer.data.remote.APIService
import com.danieloliveira138.criptoviewer.domain.model.CryptoListingsResponse
import dagger.Module
import jakarta.inject.Inject

@Module
class CryptoRepository @Inject constructor(
    private val apiService: APIService
) {
    suspend fun getCryptoListings(
        start: Int = 1,
        limit: Int = 10,
        convert: String = "USD"
    ): CryptoListingsResponse = apiService.getCryptoListings(start, limit, convert)
}
