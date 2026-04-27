package com.danieloliveira138.criptoviewer.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeInfoResponseDTO(
    val data: Map<String, ExchangeInfoDTO>?,
    val status: ExchangeStatusDTO
)
