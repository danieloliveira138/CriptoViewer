package com.danieloliveira138.criptoviewer.data.remote.mapper

import com.danieloliveira138.criptoviewer.data.remote.dto.ExchangeDTO
import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem
import jakarta.inject.Inject

class ExchangeItemMapper @Inject constructor() : Mapper<ExchangeDTO, ExchangeItem> {
    override fun map(input: ExchangeDTO): ExchangeItem {
        return ExchangeItem(
            id = input.id,
            name = input.name,
            isActive = input.isActive == 1
        )
    }
}