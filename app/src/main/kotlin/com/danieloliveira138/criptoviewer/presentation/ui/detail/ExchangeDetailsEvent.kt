package com.danieloliveira138.criptoviewer.presentation.ui.detail

sealed class ExchangeDetailsEvent {
    data class OnLinkClicked(val link: String) : ExchangeDetailsEvent()
    data object OnBackClicked : ExchangeDetailsEvent()
}