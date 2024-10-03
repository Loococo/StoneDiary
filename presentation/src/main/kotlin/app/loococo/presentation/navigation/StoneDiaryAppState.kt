package app.loococo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.util.trace
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import app.loococo.presentation.screen.home.homeRoute
import app.loococo.presentation.screen.home.navigateToHome

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
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            homeRoute -> TopLevelDestination.HOME
            else -> null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace(sectionName = "Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }

            when (topLevelDestination) {
                TopLevelDestination.HOME -> {
                    navController.navigateToHome(topLevelNavOptions)
                }

                TopLevelDestination.CONTENT ->{
                    navController.navigateToHome(topLevelNavOptions)
                }
                TopLevelDestination.MY ->{
                    navController.navigateToHome(topLevelNavOptions)
                }
            }
        }
    }
}