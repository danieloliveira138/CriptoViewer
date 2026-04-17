package com.danieloliveira138.criptoviewer.data.remote

import retrofit2.http.GET

interface APIService {
    @GET("coins")
    suspend fun getCoins(): List<String>
}
