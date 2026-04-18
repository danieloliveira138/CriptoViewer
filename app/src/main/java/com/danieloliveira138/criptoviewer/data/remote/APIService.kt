package com.danieloliveira138.criptoviewer.data.remote

import com.danieloliveira138.criptoviewer.domain.model.ExchangeInfoResponse
import com.danieloliveira138.criptoviewer.domain.model.ExchangeMapResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("v1/exchange/map")
    suspend fun getExchanges(
        @Query("start") start: Int,
        @Query("limit") limit: Int,
    ): ExchangeMapResponse

    @GET("v1/exchange/info")
    suspend fun getExchangeInfo(
        @Query("id") id: Int,
    ): ExchangeInfoResponse
}
