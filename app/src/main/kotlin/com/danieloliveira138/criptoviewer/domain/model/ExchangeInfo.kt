package com.danieloliveira138.criptoviewer.domain.model

data class ExchangeInfo(
    val id: Int,
    val name: String?,
    val slug: String?,
    val logo: String?,
    val description: String?,
    val dateLaunched: String?,
    val urls: ExchangeUrl?,
    val makerFee: Double?,
    val takerFee: Double?,
)
