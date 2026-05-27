package app.loococo.presentation.screen.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import app.loococo.domain.model.image.CropData
import app.loococo.domain.model.image.CropSize
import app.loococo.domain.model.image.ImageData
import app.loococo.domain.usecase.GalleryUseCase
import app.loococo.domain.usecase.ImageCalculateUesCase
import app.loococo.domain.usecase.ImageCropUseCase
import app.loococo.domain.usecase.ImageSaveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * 갤러리(이미지 선택·크롭) 화면 ViewModel — 순수 Flow MVI (Orbit 제거, Phase 5).
 */
@HiltViewModel
class GalleryViewModel @Inject constructor(
    galleryUseCase: GalleryUseCase,
    private val imageCalculateUseCase: ImageCalculateUesCase,
    private val imageCropUseCase: ImageCropUseCase,
    private val imageSaveUseCase: ImageSaveUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GalleryUiState())
    val state: StateFlow<GalleryUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<GalleryUiEffect>(
        replay = 0,
        extraBufferCapacity = 1,
    )
    val effect: SharedFlow<GalleryUiEffect> = _effect.asSharedFlow()

    val imagePager: Flow<PagingData<ImageData>> = galleryUseCase.getImages()

    init {
        onEvent(GalleryUiEvent.OnFirstImage(galleryUseCase.getFirstImage()))
    }

    fun onEvent(event: GalleryUiEvent) {
        when (event) {
            GalleryUiEvent.OnBackClicked -> emitEffect(GalleryUiEffect.NavigateUp)
            is GalleryUiEvent.OnImageClicked -> _state.update { it.copy(imageData = event.imageData) }
            GalleryUiEvent.OnSelectedClicked -> handleSelectedClicked()
            is GalleryUiEvent.OnUpdateZoomData -> _state.update { it.copy(cropData = event.cropData) }
            is GalleryUiEvent.OnFirstImage -> _state.update { it.copy(imageData = event.imageData) }
        }
    }

    private fun handleSelectedClicked() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val rect = imageCropUseCase.copRect(state.value.cropData)
                val image = imageCropUseCase.cropImage(state.value.imageData, state.value.cropData, rect)
                val result = imageSaveUseCase.saveCropImage(image)
                _effect.emit(GalleryUiEffect.NavigateToWrite(result))
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun emitEffect(effect: GalleryUiEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }

    fun calculateImageSize(imageData: ImageData, boxSize: CropSize): CropSize {
        return imageCalculateUseCase.calculateImageSize(imageData, boxSize)
    }

    fun calculateScaleFactor(imageSize: CropSize, boxSize: CropSize): Float {
        return imageCalculateUseCase.calculateScaleFactor(imageSize, boxSize)
    }
}
