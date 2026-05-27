package app.loococo.presentation.screen.auth.register

import androidx.compose.runtime.Immutable

@Immutable
data class RegisterUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val name: String = ""
)

sealed class RegisterUiEffect {
    data class ShowToast(val res: String) : RegisterUiEffect()
}

sealed class RegisterUiEvent {
    data class OnEmailUpdated(val email: String) : RegisterUiEvent()
    data class OnPasswordUpdated(val password: String) : RegisterUiEvent()
    data class OnNameUpdated(val name: String) : RegisterUiEvent()
    data object OnRegisterClicked : RegisterUiEvent()
}