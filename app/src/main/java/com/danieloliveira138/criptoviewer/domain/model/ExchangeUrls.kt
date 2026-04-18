package com.danieloliveira138.criptoviewer.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeUrls(
    val website: List<String>,
    val twitter: List<String>,
    val blog: List<String>,
    val chat: List<String>,
    val fee: List<String>
)
