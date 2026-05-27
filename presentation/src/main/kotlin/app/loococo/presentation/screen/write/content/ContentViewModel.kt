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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * 다이어리 본문 작성 ViewModel — 순수 Flow MVI (Orbit 제거, Phase 5).
 */
@HiltViewModel
class ContentViewModel @Inject constructor(
    private val diaryUseCase: DiaryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ContentUiState())
    val state: StateFlow<ContentUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ContentUiEffect>(
        replay = 0,
        extraBufferCapacity = 1,
    )
    val effect: SharedFlow<ContentUiEffect> = _effect.asSharedFlow()

    private val emotion = savedStateHandle.toRoute<AppRoute.Write.Content>().emotion
    private val id = savedStateHandle.toRoute<AppRoute.Write.Content>().id

    init {
        onEvent(ContentUiEvent.OnEmotionUpdated(emotion))
        onEvent(ContentUiEvent.OnDiaryIdUpdated(id))
    }

    fun onEvent(event: ContentUiEvent) {
        when (event) {
            ContentUiEvent.OnAddImageClicked -> handleAddImageClicked()
            ContentUiEvent.OnBackClicked -> emitEffect(ContentUiEffect.NavigateUp)
            ContentUiEvent.OnConfirmDeleteImage -> handleConfirmDeleteImage()
            is ContentUiEvent.OnContentUpdated -> _state.update { it.copy(content = event.content) }
            is ContentUiEvent.OnDeleteImageClicked -> handleDeleteImageClicked(event.image)
            is ContentUiEvent.OnEmotionUpdated -> _state.update { it.copy(emotion = event.emotion.formatEmotionEnum()) }
            is ContentUiEvent.OnImageAdded -> handleImageAdded(event.image)
            ContentUiEvent.OnSaveClicked -> handleSaveClicked()
            is ContentUiEvent.OnTitleUpdated -> _state.update { it.copy(title = event.title) }
            is ContentUiEvent.OnDiaryIdUpdated -> handleDiaryIdUpdated(event.id)
        }
    }

    private fun handleDiaryIdUpdated(id: Long) {
        if (id == 0L) return
        viewModelScope.launch(Dispatchers.IO) {
            diaryUseCase.getDiary(id).collectLatest { diary ->
                _state.update {
                    it.copy(
                        id = id,
                        title = diary.title,
                        content = diary.content,
                        imageList = diary.imageList.toMutableList().ifEmpty { mutableListOf() }
                    )
                }
            }
        }
    }

    private fun handleConfirmDeleteImage() {
        _state.update {
            val newImageList = it.imageList.toMutableList().apply { remove(it.selectedImage) }
            it.copy(imageList = newImageList)
        }
    }

    private fun handleDeleteImageClicked(image: String) {
        _state.update { it.copy(selectedImage = image) }
        emitEffect(ContentUiEffect.DeleteImageDialog)
    }

    private fun handleImageAdded(image: String) {
        if (image.isBlank() || state.value.imageList.contains(image)) return
        _state.update {
            val newImageList = it.imageList.toMutableList().apply { add(image) }
            it.copy(imageList = newImageList)
        }
    }

    private fun handleSaveClicked() {
        if (state.value.title.isBlank() || state.value.content.isBlank()) {
            emitEffect(ContentUiEffect.ShowToast(R.string.write_content_waring))
            return
        }

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val s = state.value
                diaryUseCase.insertOrUpdate(
                    s.id,
                    s.currentDate,
                    s.title,
                    s.content,
                    s.emotion.name,
                    s.imageList
                )
            }
            _effect.emit(ContentUiEffect.NavigateToHome)
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun handleAddImageClicked() {
        if (state.value.imageList.size == 3) {
            emitEffect(ContentUiEffect.ShowToast(R.string.image_limit_waring))
            return
        }
        emitEffect(ContentUiEffect.NavigateToGallery)
    }

    private fun emitEffect(effect: ContentUiEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
