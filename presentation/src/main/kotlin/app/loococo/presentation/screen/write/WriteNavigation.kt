package app.loococo.presentation.screen.write

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val writeRoute = "write_route"

fun NavGraphBuilder.writeScreen() {
    composable(route = writeRoute) {
        WriteRoute()
    }
}

fun NavController.navigateToWrite() {
    this.navigate(writeRoute)
}