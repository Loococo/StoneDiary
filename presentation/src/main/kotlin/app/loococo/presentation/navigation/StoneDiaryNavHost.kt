package app.loococo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import app.loococo.presentation.screen.detail.detailScreen
import app.loococo.presentation.screen.detail.navigateToDetail
import app.loococo.presentation.screen.home.homeRoute
import app.loococo.presentation.screen.home.homeScreen
import app.loococo.presentation.screen.write.navigateToWrite
import app.loococo.presentation.screen.write.writeScreen

@Composable
fun StoneDiaryNavHost(
    appState: StoneDiaryAppState
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = homeRoute
    ) {
        homeScreen(
            onDetail = navController::navigateToDetail,
            onWrite = navController::navigateToWrite
        )
        detailScreen()
        writeScreen()
    }
}