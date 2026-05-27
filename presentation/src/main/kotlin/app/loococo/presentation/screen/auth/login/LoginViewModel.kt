package app.loococo.presentation.screen.auth.login

import androidx.lifecycle.ViewModel
import app.loococo.domain.error.ErrorMessageHandler
import app.loococo.domain.model.network.Resource
import app.loococo.domain.usecase.LoginUseCase
import app.loococo.domain.usecase.PreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val preferencesUseCase: PreferencesUseCase,
    private val errorMessageHandler: ErrorMessageHandler
) :
    ContainerHost<LoginUiState, LoginUiEffect>, ViewModel() {

    override val container = container<LoginUiState, LoginUiEffect>(LoginUiState())

    fun onEventReceived(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnEmailUpdated -> onEmailUpdated(event.email)
            is LoginUiEvent.OnPasswordUpdated -> onPasswordUpdated(event.password)
            LoginUiEvent.OnLoginClicked -> onLoginClicked()
            LoginUiEvent.OnRegisterClicked -> onRegisterClicked()
            LoginUiEvent.OnSkipLoginClicked -> onNoLoginClicked()
        }
    }

    private fun onNoLoginClicked() = intent {
        preferencesUseCase.saveSkipLoginState()
        postSideEffect(LoginUiEffect.NavigateToHome)
    }

    private fun onRegisterClicked() = intent {
        postSideEffect(LoginUiEffect.NavigateToRegister)
    }

    private fun onLoginClicked() = intent {
        reduce { state.copy(isLoading = true) }

        loginUseCase(state.email, state.password).collectLatest { response ->
            when (response) {
                is Resource.Success -> {
                    preferencesUseCase.saveLoginData(response.data)
                    postSideEffect(LoginUiEffect.NavigateToHome)
                }

                is Resource.Error -> {
                    val errorMessage = errorMessageHandler.getErrorMessage(response.error)
                    errorMessage?.let {
                        postSideEffect(LoginUiEffect.ShowToast(errorMessage))
                    }
                }
            }
        }

        reduce { state.copy(isLoading = false) }
    }

    private fun onPasswordUpdated(password: String) = intent {
        reduce { state.copy(password = password) }
    }

    private fun onEmailUpdated(email: String) = intent {
        reduce { state.copy(email = email) }
    }
}