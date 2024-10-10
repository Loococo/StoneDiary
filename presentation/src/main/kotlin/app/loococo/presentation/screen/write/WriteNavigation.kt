package app.loococo.presentation.screen.write

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import app.loococo.presentation.screen.write.content.ContentRoute
import app.loococo.presentation.screen.write.emotion.EmotionRoute

const val writeRoute = "write"
const val emotionRoute = "emotion"
const val contentRoute = "content"

fun NavGraphBuilder.writeScreen(navigateToWrite: (String) -> Unit, navigateToHome: () -> Unit, navigateUp: () -> Unit) {
    navigation(startDestination = emotionRoute, route = writeRoute) {
        composable(route = emotionRoute) {
            EmotionRoute(navigateToWrite, navigateUp)
        }

        composable(
            route = "$contentRoute/{emotion}",
            arguments = listOf(navArgument("emotion") { type = NavType.StringType })
        ) { entry ->
            val emotion = entry.arguments?.getString("emotion") ?: ""
            ContentRoute(emotion, navigateToHome, navigateUp)
        }
    }

}

fun NavController.navigateToEmotion() {
    this.navigate(emotionRoute)
}

fun NavController.navigateToWrite(emotion: String) {
    this.navigate("$contentRoute/$emotion")
}