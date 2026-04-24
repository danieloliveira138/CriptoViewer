package com.danieloliveira138.criptoviewer.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeUrlsDTO(
    @SerialName("website") val website: List<String>? = null,
    @SerialName("twitter") val twitter: List<String>? = null,
    @SerialName("facebook") val facebook: List<String>? = null,
    @SerialName("blog") val blog: List<String>? = null,
    @SerialName("chat") val chat: List<String>? = null,
    @SerialName("fee") val fee: List<String>? = null
)
