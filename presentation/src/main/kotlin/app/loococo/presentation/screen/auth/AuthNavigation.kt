package app.loococo.presentation.screen.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.loococo.presentation.screen.AppRoute
import app.loococo.presentation.screen.auth.login.LoginRoute
import app.loococo.presentation.screen.auth.register.RegisterRoute

fun NavGraphBuilder.authScreen(navigateUpToHome: () -> Unit, navigateUpToRegister: () -> Unit) {
    navigation<AppRoute.Auth>(startDestination = AppRoute.Auth.Login::class) {
        composable<AppRoute.Auth.Login> {
            LoginRoute(navigateUpToHome, navigateUpToRegister)
        }
        composable<AppRoute.Auth.Register> {
            RegisterRoute()
        }
    }
}

fun NavController.navigateUpToRegister() {
    this.navigate(AppRoute.Auth.Register)
}
