package app.loococo.presentation.screen.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.loococo.presentation.screen.AppRoute

fun NavGraphBuilder.detailScreen(navigateToHome: () -> Unit, navigateUp: () -> Unit) {
    composable<AppRoute.Detail> {
        DetailRoute(navigateToHome, navigateUp)
    }
}

fun NavController.navigateToDetail(id: Long) {
    this.navigate(AppRoute.Detail(id))
}