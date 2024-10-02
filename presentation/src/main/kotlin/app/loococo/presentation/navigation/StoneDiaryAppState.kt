package app.loococo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberStoneDiaryAppState(
    navController: NavHostController = rememberNavController(),
): StoneDiaryAppState {
    return remember(navController) {
        StoneDiaryAppState(
            navController = navController
        )
    }
}

@Stable
class StoneDiaryAppState(
    val navController: NavHostController
) {

}