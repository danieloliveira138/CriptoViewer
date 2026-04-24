package com.danieloliveira138.criptoviewer.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeMapResponseDTO(
    val data: List<ExchangeDTO>?,
    val status: ExchangeStatusDTO
)
