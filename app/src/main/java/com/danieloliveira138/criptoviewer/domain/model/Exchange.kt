package com.danieloliveira138.criptoviewer.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Exchange(
    val id: Int,
    val name: String,
    val slug: String,
    @SerialName("first_historical_data") val firstHistoricalData: String?,
    @SerialName("last_historical_data") val lastHistoricalData: String?,
    @SerialName("is_active") val isActive: Int?,
    @SerialName("is_listed") val isListed: Int?,
    @SerialName("is_redistributable") val isRedistributable: Int?,
)
