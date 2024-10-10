package app.loococo.presentation.screen.write.emotion

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class EmotionViewModel @Inject constructor() :
    ContainerHost<EmotionState, EmotionSideEffect>, ViewModel() {
    override val container = container<EmotionState, EmotionSideEffect>(EmotionState())

    fun handleIntent(intent: EmotionEvent) {
        when (intent) {
            EmotionEvent.BackClickEvent -> navigateUp()
            is EmotionEvent.EmotionClickEvent -> navigateToWrite(intent.emotion)
        }
    }

    private fun navigateToWrite(emotion: String) = intent {
        reduce { state.copy(emotion = emotion) }
        postSideEffect(EmotionSideEffect.NavigateToWrite)
    }

    private fun navigateUp() = intent {
        postSideEffect(EmotionSideEffect.NavigateUp)
    }
}