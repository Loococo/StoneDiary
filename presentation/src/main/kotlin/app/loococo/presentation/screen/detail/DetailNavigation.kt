package app.loococo.presentation.screen.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val detailRoute = "detail_route"

fun NavGraphBuilder.detailScreen() {
    composable(route = detailRoute) {
        DetailRoute()
    }
}

fun NavController.navigateToDetail() {
    this.navigate(detailRoute)
}