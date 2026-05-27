package app.loococo.presentation.screen.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.loococo.domain.error.ErrorMessageHandler
import app.loococo.domain.model.network.Resource
import app.loococo.domain.usecase.RegisterUseCase
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
 * 회원가입 화면 ViewModel — 순수 Flow MVI (Orbit 제거, Phase 5).
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val errorMessageHandler: ErrorMessageHandler
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RegisterUiEffect>(
        replay = 0,
        extraBufferCapacity = 1,
    )
    val effect: SharedFlow<RegisterUiEffect> = _effect.asSharedFlow()

    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.OnEmailUpdated -> _state.update { it.copy(email = event.email) }
            is RegisterUiEvent.OnPasswordUpdated -> _state.update { it.copy(password = event.password) }
            is RegisterUiEvent.OnNameUpdated -> _state.update { it.copy(name = event.name) }
            RegisterUiEvent.OnRegisterClicked -> handleRegisterClicked()
        }
    }

    private fun handleRegisterClicked() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            registerUseCase(state.value.email, state.value.password, state.value.name)
                .collectLatest { response ->
                    when (response) {
                        is Resource.Success -> { /* TODO: 가입 성공 처리 (현재 미구현) */ }
                        is Resource.Error -> {
                            errorMessageHandler.getErrorMessage(response.error)?.let { msg ->
                                _effect.emit(RegisterUiEffect.ShowToast(msg))
                            }
                        }
                    }
                }

            _state.update { it.copy(isLoading = false) }
        }
    }
}
