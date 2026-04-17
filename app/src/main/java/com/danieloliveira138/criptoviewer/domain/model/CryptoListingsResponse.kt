package com.danieloliveira138.criptoviewer.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CryptoListingsResponse(
    val status: CryptoStatus,
    val data: List<CryptoCoin>
)

@Serializable
data class CryptoStatus(
    val timestamp: String,
    @SerialName("error_code") val errorCode: Int,
    @SerialName("error_message") val errorMessage: String? = null,
    val elapsed: Int,
    @SerialName("credit_count") val creditCount: Int,
    @SerialName("total_count") val totalCount: Int? = null
)

@Serializable
data class CryptoCoin(
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    @SerialName("cmc_rank") val cmcRank: Int,
    @SerialName("num_market_pairs") val numMarketPairs: Int,
    @SerialName("circulating_supply") val circulatingSupply: Double,
    @SerialName("total_supply") val totalSupply: Double,
    @SerialName("max_supply") val maxSupply: Double? = null,
    @SerialName("date_added") val dateAdded: String,
    @SerialName("last_updated") val lastUpdated: String,
    val quote: Map<String, CryptoQuote>
)

@Serializable
data class CryptoQuote(
    val price: Double,
    @SerialName("volume_24h") val volume24h: Double,
    @SerialName("volume_change_24h") val volumeChange24h: Double,
    @SerialName("percent_change_1h") val percentChange1h: Double,
    @SerialName("percent_change_24h") val percentChange24h: Double,
    @SerialName("percent_change_7d") val percentChange7d: Double,
    @SerialName("market_cap") val marketCap: Double,
    @SerialName("market_cap_dominance") val marketCapDominance: Double,
    @SerialName("fully_diluted_market_cap") val fullyDilutedMarketCap: Double,
    @SerialName("last_updated") val lastUpdated: String
)
