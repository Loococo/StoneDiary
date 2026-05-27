package app.loococo.presentation.screen.auth.login


data class LoginUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = ""
)

sealed class LoginUiEffect {
    data object NavigateToHome : LoginUiEffect()
    data object NavigateToRegister : LoginUiEffect()
    data class ShowToast(val res: String) : LoginUiEffect()
}

sealed class LoginUiEvent {
    data class OnEmailUpdated(val email: String) : LoginUiEvent()
    data class OnPasswordUpdated(val password: String) : LoginUiEvent()
    data object OnLoginClicked : LoginUiEvent()
    data object OnRegisterClicked : LoginUiEvent()
    data object OnSkipLoginClicked : LoginUiEvent()
}