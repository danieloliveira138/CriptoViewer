package com.danieloliveira138.criptoviewer.presentation.ui.detail

sealed class DetailListEvent {
    data class OnLinkClicked(val link: String) : DetailListEvent()
    data object OnBackClicked : DetailListEvent()
}