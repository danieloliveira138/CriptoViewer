package com.danieloliveira138.criptoviewer.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeUrlsDTO(
    val website: List<String>,
    val twitter: List<String>,
    val blog: List<String>,
    val chat: List<String>,
    val fee: List<String>
)
