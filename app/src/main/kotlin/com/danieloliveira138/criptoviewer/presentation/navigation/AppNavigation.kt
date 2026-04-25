package com.danieloliveira138.criptoviewer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.danieloliveira138.criptoviewer.presentation.ui.detail.detailsListScreen
import com.danieloliveira138.criptoviewer.presentation.ui.list.mainListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.MainListScreen.route,
    ) {
        mainListScreen(navController = navController)
        detailsListScreen(navController = navController)
    }
}
