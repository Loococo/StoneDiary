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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val diaryUseCase: DiaryUseCase,
    savedStateHandle: SavedStateHandle
) :
    ContainerHost<ContentUiState, ContentUiEffect>, ViewModel() {
    override val container = container<ContentUiState, ContentUiEffect>(ContentUiState())

    private val emotion = savedStateHandle.toRoute<AppRoute.Write.Content>().emotion
    private val id = savedStateHandle.toRoute<AppRoute.Write.Content>().id

    init {
        onEventReceived(ContentUiEvent.OnEmotionUpdated(emotion))
        onEventReceived(ContentUiEvent.OnDiaryIdUpdated(id))
    }

    fun onEventReceived(event: ContentUiEvent) {
        when (event) {
            ContentUiEvent.OnAddImageClicked -> onAddImageClicked()
            ContentUiEvent.OnBackClicked -> onBackClicked()
            ContentUiEvent.OnConfirmDeleteImage -> onConfirmDeleteImage()
            is ContentUiEvent.OnContentUpdated -> onContentUpdated(event.content)
            is ContentUiEvent.OnDeleteImageClicked -> onDeleteImageClicked(event.image)
            is ContentUiEvent.OnEmotionUpdated -> onEmotionUpdated(event.emotion)
            is ContentUiEvent.OnImageAdded -> onImageAdded(event.image)
            ContentUiEvent.OnSaveClicked -> onSaveClicked()
            is ContentUiEvent.OnTitleUpdated -> onTitleUpdated(event.title)
            is ContentUiEvent.OnDiaryIdUpdated -> onDiaryIdUpdated(event.id)
        }
    }

    private fun onDiaryIdUpdated(id: Long) = intent {
        if (id != 0L) {
            viewModelScope.launch(Dispatchers.IO) {
                diaryUseCase.getDiary(id).collectLatest { diary ->
                    reduce {
                        state.copy(
                            id = id,
                            title = diary.title,
                            content = diary.content,
                            imageList = diary.imageList.toMutableList().ifEmpty { mutableListOf() }
                        )
                    }
                }
            }
        }
    }

    private fun onConfirmDeleteImage() = intent {
        val newImageList = state.imageList.toMutableList().apply { remove(state.selectedImage) }
        reduce { state.copy(imageList = newImageList) }
    }

    private fun onDeleteImageClicked(image: String) = intent {
        reduce { state.copy(selectedImage = image) }
        postSideEffect(ContentUiEffect.DeleteImageDialog)
    }

    private fun onImageAdded(image: String) = intent {
        if (image.isBlank() || state.imageList.contains(image)) return@intent
        val newImageList = state.imageList.toMutableList().apply { add(image) }
        reduce { state.copy(imageList = newImageList) }
    }

    private fun onSaveClicked() = intent {
        if (state.title.isBlank() || state.content.isBlank()) {
            postSideEffect(ContentUiEffect.ShowToast(R.string.write_content_waring))
            return@intent
        }

        reduce { state.copy(isLoading = true) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                diaryUseCase.insertOrUpdate(
                    state.id,
                    state.currentDate,
                    state.title,
                    state.content,
                    state.emotion.name,
                    state.imageList
                )
            }
            postSideEffect(ContentUiEffect.NavigateToHome)

            reduce { state.copy(isLoading = false) }
        }
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
            postSideEffect(ContentUiEffect.ShowToast(R.string.image_limit_waring))
            return@intent
        }
        postSideEffect(ContentUiEffect.NavigateToGallery)
    }

    private fun onBackClicked() = intent {
        postSideEffect(ContentUiEffect.NavigateUp)
    }
}