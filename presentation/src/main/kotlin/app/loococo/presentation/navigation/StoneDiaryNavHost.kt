package app.loococo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import app.loococo.presentation.screen.home.homeRoute
import app.loococo.presentation.screen.home.homeScreen

@Composable
fun StoneDiaryNavHost(
    appState: StoneDiaryAppState
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = homeRoute
    ) {
        homeScreen()
    }
}