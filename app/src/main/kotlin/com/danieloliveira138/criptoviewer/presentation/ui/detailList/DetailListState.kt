package com.danieloliveira138.criptoviewer.presentation.ui.detailList

import com.danieloliveira138.criptoviewer.domain.model.CoinItem
import com.danieloliveira138.criptoviewer.domain.model.ExchangeInfo

data class DetailListState(
    val isLoading: Boolean = false,
    val exchangeInfo: ExchangeInfo? = null,
    val exchangeAssets: List<CoinItem> = emptyList(),
    val error: String? = null
)