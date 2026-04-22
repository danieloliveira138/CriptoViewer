package com.danieloliveira138.criptoviewer.domain.usecase

import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem
import com.danieloliveira138.criptoviewer.domain.repository.ExchangeRepository
import javax.inject.Inject

class ExchangeUseCase @Inject constructor(
    private val repository: ExchangeRepository,
) {
    suspend fun getExchangesList(start: Int, limit: Int): List<ExchangeItem> {
        return repository.getExchangesList(start, limit)
    }
}
