package com.danieloliveira138.criptoviewer.data.repository

import com.danieloliveira138.criptoviewer.data.remote.APIService
import com.danieloliveira138.criptoviewer.domain.model.ExchangeInfoResponse
import com.danieloliveira138.criptoviewer.domain.model.ExchangeMapResponse
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import jakarta.inject.Inject

@Module
@InstallIn(ViewModelComponent::class)
class CryptoRepository @Inject constructor(
    private val apiService: APIService
) {
    suspend fun getExchangesList(
        start: Int,
        limit: Int,
    ): ExchangeMapResponse {
        return try {
            apiService.getExchanges(start, limit)
        } catch(exception: Exception) {
            println(exception)
            throw exception
        }
    }

    suspend fun getExchangeInfo(
        id: Int
    ): ExchangeInfoResponse = apiService.getExchangeInfo(id)
}
