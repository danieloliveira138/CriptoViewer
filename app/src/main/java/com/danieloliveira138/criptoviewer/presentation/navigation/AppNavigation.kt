package com.danieloliveira138.criptoviewer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.danieloliveira138.criptoviewer.presentation.ui.mainList.MAIN_LIST_ROUTE
import com.danieloliveira138.criptoviewer.presentation.ui.mainList.mainListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MAIN_LIST_ROUTE,
    ) {
        mainListScreen(navController = navController)
    }
}
