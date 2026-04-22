package com.danieloliveira138.criptoviewer.data.repository

import com.danieloliveira138.criptoviewer.data.remote.api.ExchangeAPI
import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeInfoResponseDTO
import com.danieloliveira138.criptoviewer.data.remote.mapper.ExchangeItemMapper
import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem
import com.danieloliveira138.criptoviewer.domain.repository.ExchangeRepository
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val exchangeApi: ExchangeAPI,
    private val exchangeItemMapper: ExchangeItemMapper
) : ExchangeRepository {

    override suspend fun getExchangesList(start: Int, limit: Int): List<ExchangeItem> {
        return try {
            exchangeApi.getExchanges(start, limit).data?.map {
                exchangeItemMapper.map(input = it)
            }.orEmpty()
        } catch (exception: Exception) {
            println(exception)
            throw exception
        }
    }

    override suspend fun getExchangeInfo(exchangeId: Int): ExchangeInfoResponseDTO =
        exchangeApi.getExchangeInfo(exchangeId)
}
