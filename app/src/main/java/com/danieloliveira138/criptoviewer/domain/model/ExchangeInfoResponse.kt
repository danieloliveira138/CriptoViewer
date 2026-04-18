package com.danieloliveira138.criptoviewer.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeInfoResponse(
    val data: Map<String, ExchangeInfo>,
    val status: ExchangeStatus
)
