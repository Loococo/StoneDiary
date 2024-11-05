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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    galleryUseCase: GalleryUseCase,
    private val imageCalculateUseCase: ImageCalculateUesCase,
    private val imageCropUseCase: ImageCropUseCase,
    private val imageSaveUseCase: ImageSaveUseCase
) : ContainerHost<GalleryState, GallerySideEffect>, ViewModel() {
    override val container = container<GalleryState, GallerySideEffect>(GalleryState())

    val imagePager: Flow<PagingData<ImageData>> = galleryUseCase.getImages()

    init {
        onEventReceived(GalleryEvent.OnFirstImage(galleryUseCase.getFirstImage()))
    }

    fun onEventReceived(event: GalleryEvent) {
        when (event) {
            GalleryEvent.OnBackClicked -> onBackClicked()
            is GalleryEvent.OnImageClicked -> onImageClicked(event.imageData)
            GalleryEvent.OnSelectedClicked -> onSelectedClicked()
            is GalleryEvent.OnUpdateZoomData -> onUpdateZoomData(event.cropData)
            is GalleryEvent.OnFirstImage -> onFirstImage(event.imageData)
        }
    }

    private fun onFirstImage(imageData: ImageData) = intent {
        reduce { state.copy(imageData = imageData) }
    }

    private fun onUpdateZoomData(cropData: CropData) = intent {
        reduce { state.copy(cropData = cropData) }
    }

    private fun onImageClicked(imageData: ImageData) = intent {
        reduce { state.copy(imageData = imageData) }
    }

    private fun onSelectedClicked() = intent {
        reduce { state.copy(isLoading = true) }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val rect = imageCropUseCase.copRect(state.cropData)
                val image = imageCropUseCase.cropImage(state.imageData, state.cropData, rect)
                val result = imageSaveUseCase.saveCropImage(image)
                postSideEffect(GallerySideEffect.NavigateToWrite(result))
            }
            reduce { state.copy(isLoading = false) }
        }
    }

    private fun onBackClicked() = intent {
        postSideEffect(GallerySideEffect.NavigateUp)
    }

    fun calculateImageSize(imageData: ImageData, boxSize: CropSize): CropSize {
        return imageCalculateUseCase.calculateImageSize(imageData, boxSize)
    }

    fun calculateScaleFactor(imageSize: CropSize, boxSize: CropSize): Float {
        return imageCalculateUseCase.calculateScaleFactor(imageSize, boxSize)
    }
}