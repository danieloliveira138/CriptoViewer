package com.danieloliveira138.criptoviewer.domain.repository

import com.danieloliveira138.criptoviewer.domain.model.ExchangeAssets
import com.danieloliveira138.criptoviewer.domain.model.ExchangeInfo
import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem

interface ExchangeRepository {

    suspend fun getExchangesList(start: Int, limit: Int): List<ExchangeItem>

    suspend fun getExchangeInfo(exchangeId: Int): ExchangeInfo

    suspend fun getExchangeAssets(exchangeId: Int): ExchangeAssets
}
