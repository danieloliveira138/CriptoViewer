package com.danieloliveira138.criptoviewer.data.remote.mapper

import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeInfoDTO
import com.danieloliveira138.criptoviewer.domain.model.ExchangeInfo
import jakarta.inject.Inject

class ExchangeInfoMapper @Inject constructor(
    val mapper: ExchangeUrlMapper
) : Mapper<ExchangeInfoDTO, ExchangeInfo> {
    override fun map(input: ExchangeInfoDTO) = ExchangeInfo(
        id = input.id,
        name = input.name,
        slug = input.slug,
        logo = input.logo,
        description = input.description,
        dateLaunched = input.dateLaunched,
        urls = input.urls?.let { mapper.map(it) },
        makerFee = input.makerFee,
        takerFee = input.takerFee
    )
}