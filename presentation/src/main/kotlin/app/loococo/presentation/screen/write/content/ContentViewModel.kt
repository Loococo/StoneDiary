package app.loococo.presentation.screen.write.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.loococo.domain.usecase.DiaryUseCase
import app.loococo.presentation.R
import app.loococo.presentation.screen.write.emotion.formatEmotionEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(private val useCase: DiaryUseCase) :
    ContainerHost<ContentState, ContentSideEffect>, ViewModel() {
    override val container = container<ContentState, ContentSideEffect>(ContentState())

    fun handleIntent(intent: ContentEvent) {
        when (intent) {
            ContentEvent.BackClickEvent -> navigateUp()
            ContentEvent.SaveClickEvent -> saveData()
            is ContentEvent.EmotionEvent -> updateEmotion(intent.emotion)
            is ContentEvent.TitleChangedEvent -> updateTitle(intent.title)
            is ContentEvent.ContentChangedEvent -> updateContent(intent.content)
        }
    }

    private fun saveData() = intent {
        if (state.title.isBlank() || state.content.isBlank()) {
            postSideEffect(ContentSideEffect.Toast(R.string.write_content_waring))
            return@intent
        }
        viewModelScope.launch(Dispatchers.IO) {
            useCase.insert(state.currentDate, state.title, state.content, state.emotion.name)
        }
        postSideEffect(ContentSideEffect.NavigateToHome)
    }

    private fun updateEmotion(emotion: String) = intent {
        reduce {
            state.copy(
                emotion = emotion.formatEmotionEnum()
            )
        }
    }

    private fun updateTitle(title: String) = intent {
        reduce { state.copy(title = title) }
    }

    private fun updateContent(content: String) = intent {
        reduce { state.copy(content = content) }
    }

    private fun navigateUp() = intent {
        postSideEffect(ContentSideEffect.NavigateUp)
    }
}