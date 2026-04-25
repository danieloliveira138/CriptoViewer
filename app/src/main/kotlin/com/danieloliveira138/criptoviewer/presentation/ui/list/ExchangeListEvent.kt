package com.danieloliveira138.criptoviewer.presentation.ui.list

import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem

sealed class ExchangeListEvent {
    data object Refresh : ExchangeListEvent()
    data object LoadNextPage : ExchangeListEvent()
    data class OnExchangeClick(val exchange: ExchangeItem) : ExchangeListEvent()
}
