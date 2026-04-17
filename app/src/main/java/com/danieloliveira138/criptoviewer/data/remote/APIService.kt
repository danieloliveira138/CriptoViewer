package com.danieloliveira138.criptoviewer.data.remote

import com.danieloliveira138.criptoviewer.domain.model.CryptoListingsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getCryptoListings(
        @Query("start") start: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("convert") convert: String = "USD"
    ): CryptoListingsResponse
}
