package app.loococo.presentation.screen.auth.register

import androidx.compose.runtime.Immutable

@Immutable
data class RegisterState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val name: String = ""
)

sealed class RegisterSideEffect {
    data class ShowToast(val res: String) : RegisterSideEffect()
}

sealed class RegisterEvent {
    data class OnEmailUpdated(val email: String) : RegisterEvent()
    data class OnPasswordUpdated(val password: String) : RegisterEvent()
    data class OnNameUpdated(val name: String) : RegisterEvent()
    data object OnRegisterClicked : RegisterEvent()
}