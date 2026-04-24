package com.danieloliveira138.criptoviewer.presentation.ui.detailList

sealed class ExchangeDetailEffect {

    data class navigateToBrowser(val url: String) : ExchangeDetailEffect()
    data object navigateBack : ExchangeDetailEffect()
    data class showToast(val message: String) : ExchangeDetailEffect()
}