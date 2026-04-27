package com.danieloliveira138.criptoviewer.domain.model

data class ExchangeAsset(
    val currencyName: String,
    val currencySymbol: String,
    val currencyPrice: Double,
)
