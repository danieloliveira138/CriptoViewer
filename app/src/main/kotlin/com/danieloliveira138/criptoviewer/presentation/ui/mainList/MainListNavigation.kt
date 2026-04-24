package com.danieloliveira138.criptoviewer.presentation.ui.mainList

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.danieloliveira138.criptoviewer.presentation.navigation.Screen

fun NavGraphBuilder.mainListScreen(navController: NavController) {
    composable(route = Screen.MainListScreen.route) {
        MainListScreen(navController = navController)
    }
}
