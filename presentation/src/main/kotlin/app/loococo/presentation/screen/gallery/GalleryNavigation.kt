package app.loococo.presentation.screen.gallery

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.loococo.presentation.screen.AppRoute

fun NavGraphBuilder.galleryScreen(navigateUpToWrite: (String) -> Unit) {
    composable<AppRoute.Gallery> {
        GalleryRoute(navigateUpToWrite)
    }
}

fun NavController.navigateToGallery() {
    this.navigate(AppRoute.Gallery)
}