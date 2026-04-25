package com.danieloliveira138.criptoviewer.presentation.ui.list

sealed class MainListEffect {
    data class NavigateTo(val route: String) : MainListEffect()
    data class ShowToast(val message: String) : MainListEffect()
}
