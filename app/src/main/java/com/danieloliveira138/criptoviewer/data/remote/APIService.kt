package com.danieloliveira138.criptoviewer.data.remote

import com.danieloliveira138.criptoviewer.domain.model.CryptoListingsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("v1/exchange/map")
    suspend fun getExchanges(
        @Query("start") start: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("convert") convert: String = "USD"
    ): CryptoListingsResponse

    @GET("v1/exchange/map")
    suspend fun getExchangeInfo(
        @Query("id") id: Int = 1,
    ): CryptoListingsResponse
}
