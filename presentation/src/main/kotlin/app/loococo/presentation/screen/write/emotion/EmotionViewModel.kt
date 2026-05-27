package app.loococo.presentation.screen.write.emotion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import app.loococo.presentation.screen.AppRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class EmotionViewModel @Inject constructor(savedStateHandle: SavedStateHandle) :
    ContainerHost<EmotionUiState, EmotionUiEffect>, ViewModel() {
    override val container = container<EmotionUiState, EmotionUiEffect>(EmotionUiState())

    private val id = savedStateHandle.toRoute<AppRoute.Write.Emotion>().id

    init {
        onEventReceived(EmotionUiEvent.OnDiaryIdUpdated(id))
    }

    fun onEventReceived(event: EmotionUiEvent) {
        when (event) {
            is EmotionUiEvent.OnEmotionClicked -> onEmotionClicked(event.emotion)
            is EmotionUiEvent.OnDiaryIdUpdated -> onDiaryIdUpdated()
            EmotionUiEvent.OnBackClicked -> onBackClicked()
        }
    }

    private fun onDiaryIdUpdated() = intent {
        reduce { state.copy(id = id) }
    }

    private fun onEmotionClicked(emotion: String) = intent {
        reduce { state.copy(emotion = emotion) }
        postSideEffect(EmotionUiEffect.NavigateToWrite)
    }

    private fun onBackClicked() = intent {
        postSideEffect(EmotionUiEffect.NavigateUp)
    }
}