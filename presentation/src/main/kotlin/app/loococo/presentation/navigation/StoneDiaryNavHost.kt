package app.loococo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import app.loococo.presentation.screen.AppRoute
import app.loococo.presentation.screen.auth.authScreen
import app.loococo.presentation.screen.auth.navigateUpToRegister
import app.loococo.presentation.screen.detail.detailScreen
import app.loococo.presentation.screen.detail.navigateToDetail
import app.loococo.presentation.screen.gallery.galleryScreen
import app.loococo.presentation.screen.gallery.navigateToGallery
import app.loococo.presentation.screen.home.homeScreen
import app.loococo.presentation.screen.home.navigateToHome
import app.loococo.presentation.screen.write.navigateToEmotion
import app.loococo.presentation.screen.write.navigateToWrite
import app.loococo.presentation.screen.write.navigateUpToWrite
import app.loococo.presentation.screen.write.writeScreen

@Composable
fun StoneDiaryNavHost(
    appState: StoneDiaryAppState
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = appState.startDestination
    ) {
        authScreen(
            navigateUpToHome = navController::navigateToHome,
            navigateUpToRegister = navController::navigateUpToRegister
        )
        homeScreen(
            navigateToDetail = navController::navigateToDetail,
            navigateToWrite = navController::navigateToEmotion
        )
        detailScreen(
            navigateToHome = navController::navigateToHome,
            navigateToWrite = navController::navigateToEmotion,
            navigateUp = navController::navigateUp
        )
        writeScreen(
            navigateToWrite = navController::navigateToWrite,
            navigateToHome = navController::navigateToHome,
            navigateToGallery = navController::navigateToGallery,
            navigateUp = navController::navigateUp
        )
        galleryScreen(
            navigateUpToWrite = navController::navigateUpToWrite,
            navigateUp = navController::navigateUp
        )
    }
}