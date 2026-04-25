package com.danieloliveira138.criptoviewer.presentation.ui.list

import com.danieloliveira138.criptoviewer.domain.model.ExchangeItem

data class MainListState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val exchanges: List<ExchangeItem> = emptyList(),
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true,
    val error: String? = null,
)
