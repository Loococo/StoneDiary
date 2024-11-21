package app.loococo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.loococo.presentation.MainUiState
import app.loococo.presentation.screen.AppRoute

@Composable
fun rememberStoneDiaryAppState(
    navController: NavHostController = rememberNavController(),
    uiState: MainUiState
): StoneDiaryAppState {
    return remember(navController) {
        StoneDiaryAppState(
            navController = navController,
            uiState = uiState
        )
    }
}

@Stable
class StoneDiaryAppState(
    val navController: NavHostController,
    val uiState: MainUiState
) {
//    val startDestination: Any = AppRoute.Auth
    val startDestination: Any = when (uiState) {
        MainUiState.Loading -> AppRoute.Home
        is MainUiState.Success -> if (uiState.isSkipLogin) AppRoute.Home else AppRoute.Auth
    }
}