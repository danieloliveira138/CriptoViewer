package com.danieloliveira138.criptoviewer.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeAssetResponseDTO(
    val data: List<ExchangeAssetDTO>? = null,
    val status: ExchangeStatusDTO
)
