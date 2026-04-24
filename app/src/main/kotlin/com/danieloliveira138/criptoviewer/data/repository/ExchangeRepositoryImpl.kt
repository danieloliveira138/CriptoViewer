package com.danieloliveira138.criptoviewer.data.repository

import com.danieloliveira138.criptoviewer.core.exceptions.EmptyResponseException
import com.danieloliveira138.criptoviewer.data.remote.api.ExchangeAPI
import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeInfoDTO
import com.danieloliveira138.criptoviewer.data.remote.mapper.ExchangeInfoMapper
import com.danieloliveira138.criptoviewer.data.remote.mapper.ExchangeItemMapper
import com.danieloliveira138.criptoviewer.data.remote.mapper.ExchangeAssetsMapper
import com.danieloliveira138.criptoviewer.domain.model.ExchangeAssets
import com.danieloliveira138.criptoviewer.domain.model.ExchangeInfo
import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem
import com.danieloliveira138.criptoviewer.domain.repository.ExchangeRepository
import okio.IOException
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val exchangeApi: ExchangeAPI,
    private val exchangeItemMapper: ExchangeItemMapper,
    private val exchangeInfoMapper: ExchangeInfoMapper,
    private val exchangeAssetsMapper: ExchangeAssetsMapper
) : ExchangeRepository {

    override suspend fun getExchangesList(start: Int, limit: Int): List<ExchangeItem> {
        return try {
            exchangeApi.getExchanges(start, limit).data?.map {
                exchangeItemMapper.map(input = it)
            } ?: throw EmptyResponseException()
        } catch (exception: IOException) {
            throw exception
        }
    }

    override suspend fun getExchangeInfo(exchangeId: Int): ExchangeInfo {
        try {
            val response = exchangeApi.getExchangeInfo(exchangeId)
            val exchangeInfoDTO: ExchangeInfoDTO = response.data?.get("$exchangeId")
                ?: throw EmptyResponseException()
            return exchangeInfoMapper.map(exchangeInfoDTO)
        } catch (exception: IOException) {
            throw exception
        }
    }

    override suspend fun getExchangeAssets(exchangeId: Int): ExchangeAssets {
        return try {
            val response = exchangeApi.getExchangeAssets(exchangeId)
            exchangeAssetsMapper.map(response)
        } catch (exception: IOException) {
            throw exception
        }
    }
}
