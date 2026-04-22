package com.danieloliveira138.criptoviewer.presentation.ui.mainList

import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem

sealed class MainListEvent {
    data object Refresh : MainListEvent()
    data class OnExchangeClick(val exchange: ExchangeItem) : MainListEvent()
}
