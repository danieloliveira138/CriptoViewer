package com.danieloliveira138.criptoviewer.presentation.ui.mainList

sealed class MainListEffect {
    data class NavigateTo(val route: String) : MainListEffect()
    data class ShowToast(val message: String) : MainListEffect()
}
