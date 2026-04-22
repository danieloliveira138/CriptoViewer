package com.danieloliveira138.criptoviewer.domain.repository

import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeInfoResponseDTO
import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem

interface ExchangeRepository {

    suspend fun getExchangesList(start: Int, limit: Int): List<ExchangeItem>

    suspend fun getExchangeInfo(exchangeId: Int): ExchangeInfoResponseDTO
}