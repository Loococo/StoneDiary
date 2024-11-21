package app.loococo.presentation.screen.auth.login


data class LoginState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = ""
)

sealed class LoginSideEffect {
    data object NavigateToHome : LoginSideEffect()
    data object NavigateToRegister : LoginSideEffect()
    data class ShowToast(val res: String) : LoginSideEffect()
}

sealed class LoginEvent {
    data class OnEmailUpdated(val email: String) : LoginEvent()
    data class OnPasswordUpdated(val password: String) : LoginEvent()
    data object OnLoginClicked : LoginEvent()
    data object OnRegisterClicked : LoginEvent()
    data object OnSkipLoginClicked : LoginEvent()
}