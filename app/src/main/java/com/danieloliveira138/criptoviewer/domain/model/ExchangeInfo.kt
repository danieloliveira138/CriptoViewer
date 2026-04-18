package com.danieloliveira138.criptoviewer.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeInfo(
    val id: Int,
    val name: String,
    val slug: String,
    val logo: String,
    val description: String,
    @SerialName("date_launched") val dateLaunched: String,
    val notice: String,
    val countries: List<String>,
    val fiats: List<String>,
    val tags: List<String>? = null,
    val type: String,
    @SerialName("maker_fee") val makerFee: Double,
    @SerialName("taker_fee") val takerFee: Double,
    @SerialName("weekly_visits") val weeklyVisits: Long,
    @SerialName("spot_volume_usd") val spotVolumeUsd: Double,
    @SerialName("spot_volume_last_updated") val spotVolumeLastUpdated: String,
    val urls: ExchangeUrls
)
