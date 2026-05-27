package app.loococo.presentation.screen.auth.register

import androidx.lifecycle.ViewModel
import app.loococo.domain.error.ErrorMessageHandler
import app.loococo.domain.model.network.Resource
import app.loococo.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val errorMessageHandler: ErrorMessageHandler
) : ContainerHost<RegisterUiState, RegisterUiEffect>, ViewModel() {

    override val container = container<RegisterUiState, RegisterUiEffect>(RegisterUiState())

    fun onEventReceived(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.OnEmailUpdated -> onEmailUpdated(event.email)
            is RegisterUiEvent.OnPasswordUpdated -> onPasswordUpdated(event.password)
            is RegisterUiEvent.OnNameUpdated -> onNameUpdated(event.name)
            RegisterUiEvent.OnRegisterClicked -> onRegisterClicked()
        }
    }

    private fun onRegisterClicked() = intent {
        reduce { state.copy(isLoading = true) }

        registerUseCase(state.email, state.password, state.name).collectLatest { response ->
            when (response) {
                is Resource.Success -> {
                }

                is Resource.Error -> {
                    val errorMessage = errorMessageHandler.getErrorMessage(response.error)
                    errorMessage?.let {
                        postSideEffect(RegisterUiEffect.ShowToast(errorMessage))
                    }
                }
            }
        }
        reduce { state.copy(isLoading = false) }
    }

    private fun onNameUpdated(name: String) = intent {
        reduce { state.copy(name = name) }
    }

    private fun onPasswordUpdated(password: String) = intent {
        reduce { state.copy(password = password) }
    }

    private fun onEmailUpdated(email: String) = intent {
        reduce { state.copy(email = email) }
    }
}
