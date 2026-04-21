package com.danieloliveira138.criptoviewer.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeMapResponse(
    val data: List<Exchange>?,
    val status: ExchangeStatus
)
