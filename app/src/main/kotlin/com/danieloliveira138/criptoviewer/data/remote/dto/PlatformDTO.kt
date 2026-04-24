package com.danieloliveira138.criptoviewer.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlatformDTO(
    @SerialName("crypto_id") val cryptoId: Int? = null,
    @SerialName("symbol") val symbol: String? = null,
    @SerialName("name") val name: String? = null
)
