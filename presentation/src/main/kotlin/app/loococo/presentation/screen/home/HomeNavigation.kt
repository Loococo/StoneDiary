package app.loococo.presentation.screen.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import app.loococo.presentation.screen.detail.detailRoute

const val homeRoute = "home"

fun NavGraphBuilder.homeScreen(
    navigateToDetail: (Long) -> Unit,
    navigateToWrite: () -> Unit
) {
    composable(route = homeRoute) {
        HomeRoute(navigateToDetail, navigateToWrite)
    }
}

fun NavController.navigateToHome() {
    val navOptions = NavOptions.Builder()
        .setPopUpTo(graph.startDestinationId, true)
        .build()
    this.navigate(homeRoute, navOptions)
}