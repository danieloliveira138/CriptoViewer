package com.danieloliveira138.criptoviewer.presentation.navigation

sealed class Screen(val route: String) {
    object MainListScreen : Screen("main_list_screen")
    object DetailListScreen : Screen("details/{exchangeId}") {
        fun createRoute(id: Int) = "details/$id"
    }
}