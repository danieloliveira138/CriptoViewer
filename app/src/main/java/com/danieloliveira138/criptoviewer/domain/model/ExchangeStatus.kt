package com.danieloliveira138.criptoviewer.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeStatus(
    val timestamp: String,
    @SerialName("error_code") val errorCode: Int?,
    @SerialName("error_message") val errorMessage: String?,
    val elapsed: Int?,
    @SerialName("credit_count") val creditCount: Int?,
    val notice: String?
)
