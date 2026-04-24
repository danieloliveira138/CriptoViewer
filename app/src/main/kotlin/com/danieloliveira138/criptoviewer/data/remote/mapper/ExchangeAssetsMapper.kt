package com.danieloliveira138.criptoviewer.data.remote.mapper

import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeAssetResponseDTO
import com.danieloliveira138.criptoviewer.domain.model.ExchangeAsset
import com.danieloliveira138.criptoviewer.domain.model.ExchangeAssets
import jakarta.inject.Inject

class ExchangeAssetsMapper @Inject constructor() : Mapper<ExchangeAssetResponseDTO, ExchangeAssets> {
    override fun map(input: ExchangeAssetResponseDTO): ExchangeAssets {
        return ExchangeAssets(
            data = input.data?.map { exchangeAssetDTO ->
                ExchangeAsset(
                    currencyName = exchangeAssetDTO.currency?.name ?: "",
                    currencySymbol = exchangeAssetDTO.currency?.symbol ?: "",
                    currencyPrice = exchangeAssetDTO.currency?.priceUsd ?: 0.0,
                )
            }
        )
    }
}