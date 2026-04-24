package com.danieloliveira138.criptoviewer.domain.usecase

import com.danieloliveira138.criptoviewer.domain.model.ExchangeAsset
import com.danieloliveira138.criptoviewer.domain.model.ExchangeInfo
import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem
import com.danieloliveira138.criptoviewer.domain.repository.ExchangeRepository
import com.danieloliveira138.criptoviewer.core.Result
import com.danieloliveira138.criptoviewer.core.exceptions.EmptyResponseException
import com.danieloliveira138.criptoviewer.core.exceptions.ExchangeIdIsNull
import javax.inject.Inject

class ExchangeUseCase @Inject constructor(
    private val repository: ExchangeRepository,
) {
    suspend fun getExchangesList(start: Int, limit: Int): Result<List<ExchangeItem>> {
        return try {
            val response = repository.getExchangesList(start, limit)
            Result.Success(response)
        } catch (exception: Exception) {
            Result.Error(exception = exception)
        }
    }

    suspend fun getExchangeInfo(exchangeId: String?): Result<ExchangeInfo> {
        return try {
            if (exchangeId.isNullOrEmpty()) Result.Error(ExchangeIdIsNull())
            else Result.Success(repository.getExchangeInfo(exchangeId.toInt()))
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

    suspend fun getExchangeAssets(exchangeId: String?): Result<List<ExchangeAsset>> {
        return try {
            if (exchangeId.isNullOrEmpty()) {
                Result.Error(ExchangeIdIsNull())
            } else {
                val response = repository.getExchangeAssets(exchangeId.toInt())
                if (response.data.isNullOrEmpty()) Result.Error(EmptyResponseException())
                else Result.Success(response.data)
            }
        } catch (exception: Exception) {
            Result.Error(exception = exception)
        }

    }
}
