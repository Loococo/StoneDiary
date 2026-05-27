package app.loococo.presentation.screen.write.emotion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import app.loococo.presentation.screen.AppRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 감정 선택 화면 ViewModel — 순수 Flow MVI (Orbit 제거, Phase 5).
 */
@HiltViewModel
class EmotionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(EmotionUiState())
    val state: StateFlow<EmotionUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<EmotionUiEffect>(
        replay = 0,
        extraBufferCapacity = 1,
    )
    val effect: SharedFlow<EmotionUiEffect> = _effect.asSharedFlow()

    private val id = savedStateHandle.toRoute<AppRoute.Write.Emotion>().id

    init {
        _state.update { it.copy(id = id) }
    }

    fun onEvent(event: EmotionUiEvent) {
        when (event) {
            is EmotionUiEvent.OnEmotionClicked -> handleEmotionClicked(event.emotion)
            is EmotionUiEvent.OnDiaryIdUpdated -> _state.update { it.copy(id = id) }
            EmotionUiEvent.OnBackClicked -> emitEffect(EmotionUiEffect.NavigateUp)
        }
    }

    private fun handleEmotionClicked(emotion: String) {
        _state.update { it.copy(emotion = emotion) }
        emitEffect(EmotionUiEffect.NavigateToWrite)
    }

    private fun emitEffect(effect: EmotionUiEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
