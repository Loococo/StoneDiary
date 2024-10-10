package app.loococo.presentation.screen.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import app.loococo.presentation.screen.write.content.ContentRoute
import app.loococo.presentation.screen.write.contentRoute

const val detailRoute = "detail"

fun NavGraphBuilder.detailScreen(navigateToHome: () -> Unit, navigateUp: () -> Unit) {
    composable(
        route = "$detailRoute/{id}",
        arguments = listOf(navArgument("id") { type = NavType.LongType })
    ) { entry ->
        val id = entry.arguments?.getLong("id") ?: 0L
        DetailRoute(id, navigateToHome, navigateUp)
    }
}

fun NavController.navigateToDetail(id: Long) {
    this.navigate("$detailRoute/$id")
}