package app.loococo.presentation.screen.write

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.loococo.presentation.screen.AppRoute
import app.loococo.presentation.screen.write.content.ContentRoute
import app.loococo.presentation.screen.write.emotion.EmotionRoute

fun NavGraphBuilder.writeScreen(
    navigateToWrite: (String, Long) -> Unit,
    navigateToHome: () -> Unit,
    navigateToGallery: () -> Unit,
    navigateUp: () -> Unit
) {
    navigation<AppRoute.Write>(startDestination = AppRoute.Write.Emotion::class) {
        composable<AppRoute.Write.Emotion> {
            EmotionRoute(navigateToWrite, navigateUp)
        }
        composable<AppRoute.Write.Content> { entry ->
            val image = entry.savedStateHandle.get<String>("image") ?: ""
            ContentRoute(image, navigateToHome, navigateToGallery, navigateUp)
        }
    }
}

fun NavController.navigateToEmotion(id: Long = 0L) {
    this.navigate(AppRoute.Write.Emotion(id))
}

fun NavController.navigateToWrite(emotion: String, id: Long = 0L) {
    this.navigate(AppRoute.Write.Content(emotion, id))
}

fun NavController.navigateUpToWrite(image: String) {
    this.previousBackStackEntry?.savedStateHandle?.set("image", image)
    this.popBackStack()
}