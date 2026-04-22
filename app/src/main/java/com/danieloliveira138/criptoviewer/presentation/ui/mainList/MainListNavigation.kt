package com.danieloliveira138.criptoviewer.presentation.ui.mainList

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val MAIN_LIST_ROUTE = "main_list"

fun NavGraphBuilder.mainListScreen(navController: NavController) {
    composable(route = MAIN_LIST_ROUTE) {
        MainListScreen(navController = navController)
    }
}
