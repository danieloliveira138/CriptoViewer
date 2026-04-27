package com.danieloliveira138.criptoviewer.presentation.ui.list

sealed class ExchangeListEffect {
    data class NavigateTo(val route: String) : ExchangeListEffect()
    data class ShowToast(val message: String) : ExchangeListEffect()
}
