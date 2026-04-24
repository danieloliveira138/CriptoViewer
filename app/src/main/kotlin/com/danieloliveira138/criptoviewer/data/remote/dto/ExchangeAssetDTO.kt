package com.danieloliveira138.criptoviewer.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ExchangeAssetDTO (
    @SerialName("wallet_address") val walletAddress: String? = null,
    @SerialName("balance") val balance: Double? = null,
    @SerialName("platform") val platform: PlatformDTO? = null,
    @SerialName("currency") val currency: CurrencyDTO? = null
)
