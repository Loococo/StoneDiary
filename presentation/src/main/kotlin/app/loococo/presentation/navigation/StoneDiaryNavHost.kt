package app.loococo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import app.loococo.presentation.screen.detail.detailScreen
import app.loococo.presentation.screen.detail.navigateToDetail
import app.loococo.presentation.screen.home.homeRoute
import app.loococo.presentation.screen.home.homeScreen
import app.loococo.presentation.screen.home.navigateToHome
import app.loococo.presentation.screen.write.navigateToEmotion
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
            navigateToDetail = navController::navigateToDetail,
            navigateToWrite = navController::navigateToEmotion
        )
        detailScreen(
            navigateToHome = navController::navigateToHome,
            navigateUp = navController::navigateUp
        )
        writeScreen(
            navigateToWrite = navController::navigateToWrite,
            navigateToHome = navController::navigateToHome,
            navigateUp = navController::navigateUp
        )
    }
}