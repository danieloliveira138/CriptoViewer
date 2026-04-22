package com.danieloliveira138.criptoviewer.presentation.ui.mainList

import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem

data class MainListState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val exchanges: List<ExchangeItem> = emptyList(),
    val error: String? = null,
)
