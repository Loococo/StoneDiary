package app.loococo.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.loococo.domain.usecase.PreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val preferencesUseCase: PreferencesUseCase) :
    ViewModel() {
    val uiState: StateFlow<MainUiState> = flow {
        emit(MainUiState.Loading)
        delay(2000)
        emit(MainUiState.Success(preferencesUseCase.shouldShowHomeScreen()))
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000)
    )
}

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(val isSkipLogin: Boolean) : MainUiState
}
