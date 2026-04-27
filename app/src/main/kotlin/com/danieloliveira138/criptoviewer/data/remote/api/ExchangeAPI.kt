package com.danieloliveira138.criptoviewer.data.remote.api

import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeAssetResponseDTO
import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeInfoResponseDTO
import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeMapResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeAPI {

    @GET("v1/exchange/map")
    suspend fun getExchanges(
        @Query("start") start: Int,
        @Query("limit") limit: Int,
    ): ExchangeMapResponseDTO

    @GET("v1/exchange/info")
    suspend fun getExchangeInfo(
        @Query("id") id: Int,
    ): ExchangeInfoResponseDTO

    @GET("/v1/exchange/assets")
    suspend fun getExchangeAssets(
        @Query("id") id: Int,
    ): ExchangeAssetResponseDTO
}