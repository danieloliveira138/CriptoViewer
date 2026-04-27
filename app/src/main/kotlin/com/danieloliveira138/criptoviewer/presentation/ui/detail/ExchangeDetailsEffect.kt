package com.danieloliveira138.criptoviewer.presentation.ui.detail

sealed class ExchangeDetailsEffect {

    data class navigateToBrowser(val url: String) : ExchangeDetailsEffect()
    data object navigateBack : ExchangeDetailsEffect()
    data class showToast(val message: String) : ExchangeDetailsEffect()
}