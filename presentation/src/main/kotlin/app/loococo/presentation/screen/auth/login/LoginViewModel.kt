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
    ContainerHost<LoginState, LoginSideEffect>, ViewModel() {

    override val container = container<LoginState, LoginSideEffect>(LoginState())

    fun onEventReceived(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailUpdated -> onEmailUpdated(event.email)
            is LoginEvent.OnPasswordUpdated -> onPasswordUpdated(event.password)
            LoginEvent.OnLoginClicked -> onLoginClicked()
            LoginEvent.OnRegisterClicked -> onRegisterClicked()
            LoginEvent.OnSkipLoginClicked -> onNoLoginClicked()
        }
    }

    private fun onNoLoginClicked() = intent {
        preferencesUseCase.saveSkipLoginState()
        postSideEffect(LoginSideEffect.NavigateToHome)
    }

    private fun onRegisterClicked() = intent {
        postSideEffect(LoginSideEffect.NavigateToRegister)
    }

    private fun onLoginClicked() = intent {
        reduce { state.copy(isLoading = true) }

        loginUseCase(state.email, state.password).collectLatest { response ->
            when (response) {
                is Resource.Success -> {
                    preferencesUseCase.saveLoginData(response.data)
                    postSideEffect(LoginSideEffect.NavigateToHome)
                }

                is Resource.Error -> {
                    val errorMessage = errorMessageHandler.getErrorMessage(response.error)
                    errorMessage?.let {
                        postSideEffect(LoginSideEffect.ShowToast(errorMessage))
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