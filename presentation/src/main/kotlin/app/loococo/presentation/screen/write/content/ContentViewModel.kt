package app.loococo.presentation.screen.write.content

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import app.loococo.domain.usecase.DiaryUseCase
import app.loococo.presentation.R
import app.loococo.presentation.screen.AppRoute
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
class ContentViewModel @Inject constructor(
    private val useCase: DiaryUseCase,
    savedStateHandle: SavedStateHandle
) :
    ContainerHost<ContentState, ContentSideEffect>, ViewModel() {
    override val container = container<ContentState, ContentSideEffect>(ContentState())

    private val emotion = savedStateHandle.toRoute<AppRoute.Write.Content>().emotion

    init {
        onEventReceived(ContentEvent.OnEmotionUpdated(emotion))
    }

    fun onEventReceived(event: ContentEvent) {
        when (event) {
            ContentEvent.OnAddImageClicked -> onAddImageClicked()
            ContentEvent.OnBackClicked -> onBackClicked()
            ContentEvent.OnConfirmDeleteImage -> onConfirmDeleteImage()
            is ContentEvent.OnContentUpdated -> onContentUpdated(event.content)
            is ContentEvent.OnDeleteImageClicked -> onDeleteImageClicked(event.image)
            is ContentEvent.OnEmotionUpdated -> onEmotionUpdated(event.emotion)
            is ContentEvent.OnImageAdded -> onImageAdded(event.image)
            ContentEvent.OnSaveClicked -> onSaveClicked()
            is ContentEvent.OnTitleUpdated -> onTitleUpdated(event.title)
        }
    }

    private fun onConfirmDeleteImage() = intent {
        val newImageList = state.imageList.toMutableList().apply { remove(state.selectedImage) }
        reduce { state.copy(imageList = newImageList) }
    }

    private fun onDeleteImageClicked(image: String) = intent {
        reduce { state.copy(selectedImage = image) }
        postSideEffect(ContentSideEffect.DeleteImageDialog)
    }

    private fun onImageAdded(image: String) = intent {
        if (image.isBlank()) return@intent
        val newImageList = state.imageList.toMutableList().apply { add(image) }
        reduce { state.copy(imageList = newImageList) }
    }

    private fun onSaveClicked() = intent {
        if (state.title.isBlank() || state.content.isBlank()) {
            postSideEffect(ContentSideEffect.ShowToast(R.string.write_content_waring))
            return@intent
        }
        viewModelScope.launch(Dispatchers.IO) {
            useCase.insert(
                state.currentDate,
                state.title,
                state.content,
                state.emotion.name,
                state.imageList
            )
        }
        postSideEffect(ContentSideEffect.NavigateToHome)
    }

    private fun onEmotionUpdated(emotion: String) = intent {
        reduce { state.copy(emotion = emotion.formatEmotionEnum()) }
    }

    private fun onTitleUpdated(title: String) = intent {
        reduce { state.copy(title = title) }
    }

    private fun onContentUpdated(content: String) = intent {
        reduce { state.copy(content = content) }
    }

    private fun onAddImageClicked() = intent {
        if (state.imageList.size == 3) {
            postSideEffect(ContentSideEffect.ShowToast(R.string.image_limit_waring))
            return@intent
        }
        postSideEffect(ContentSideEffect.NavigateToGallery)
    }

    private fun onBackClicked() = intent {
        postSideEffect(ContentSideEffect.NavigateUp)
    }
}