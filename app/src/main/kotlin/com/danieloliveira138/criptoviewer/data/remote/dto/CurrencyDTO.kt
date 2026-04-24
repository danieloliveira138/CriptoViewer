package com.danieloliveira138.criptoviewer.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyDTO(
    @SerialName("crypto_id") val cryptoId: Int? = null,
    @SerialName("price_usd") val priceUsd: Double? = null,
    @SerialName("symbol") val symbol: String? = null,
    @SerialName("name") val name: String? = null
)
