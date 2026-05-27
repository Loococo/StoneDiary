package app.loococo.presentation.screen.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.loococo.domain.error.ErrorMessageHandler
import app.loococo.domain.model.network.Resource
import app.loococo.domain.usecase.LoginUseCase
import app.loococo.domain.usecase.PreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 로그인 화면 ViewModel — 순수 Flow MVI (Orbit 제거, Phase 5).
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val preferencesUseCase: PreferencesUseCase,
    private val errorMessageHandler: ErrorMessageHandler
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginUiEffect>(
        replay = 0,
        extraBufferCapacity = 1,
    )
    val effect: SharedFlow<LoginUiEffect> = _effect.asSharedFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnEmailUpdated -> _state.update { it.copy(email = event.email) }
            is LoginUiEvent.OnPasswordUpdated -> _state.update { it.copy(password = event.password) }
            LoginUiEvent.OnLoginClicked -> handleLoginClicked()
            LoginUiEvent.OnRegisterClicked -> handleRegisterClicked()
            LoginUiEvent.OnSkipLoginClicked -> handleSkipLoginClicked()
        }
    }

    private fun handleSkipLoginClicked() {
        viewModelScope.launch {
            preferencesUseCase.saveSkipLoginState()
            _effect.emit(LoginUiEffect.NavigateToHome)
        }
    }

    private fun handleRegisterClicked() {
        viewModelScope.launch { _effect.emit(LoginUiEffect.NavigateToRegister) }
    }

    private fun handleLoginClicked() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            loginUseCase(state.value.email, state.value.password).collectLatest { response ->
                when (response) {
                    is Resource.Success -> {
                        preferencesUseCase.saveLoginData(response.data)
                        _effect.emit(LoginUiEffect.NavigateToHome)
                    }
                    is Resource.Error -> {
                        errorMessageHandler.getErrorMessage(response.error)?.let { msg ->
                            _effect.emit(LoginUiEffect.ShowToast(msg))
                        }
                    }
                }
            }

            _state.update { it.copy(isLoading = false) }
        }
    }
}
