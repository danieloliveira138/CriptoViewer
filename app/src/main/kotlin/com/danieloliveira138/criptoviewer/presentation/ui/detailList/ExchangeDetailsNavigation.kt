package com.danieloliveira138.criptoviewer.presentation.ui.detailList

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.danieloliveira138.criptoviewer.presentation.navigation.Screen

fun NavGraphBuilder.detailsListScreen(navController: NavController) {
    composable(route = Screen.DetailListScreen.route) {
        val id = it.arguments?.getString("id") ?: ""
        val viewModel = hiltViewModel<ExchangeDetailsViewModel>()
        ExchangeDetailsScreen(
            navController = navController,
            viewModel = viewModel)
    }
}
