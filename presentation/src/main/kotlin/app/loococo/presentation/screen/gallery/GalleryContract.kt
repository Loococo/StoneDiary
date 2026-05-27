package app.loococo.presentation.screen.gallery

import app.loococo.domain.model.image.CropData
import app.loococo.domain.model.image.ImageData

data class GalleryUiState(
    val imageData: ImageData = ImageData(),
    val cropData: CropData = CropData(),
    val isLoading: Boolean = false
)

sealed class GalleryUiEffect {
    data object NavigateUp : GalleryUiEffect()
    data class NavigateToWrite(val image: String) : GalleryUiEffect()
}

sealed class GalleryUiEvent {
    data object OnSelectedClicked : GalleryUiEvent()
    data class OnImageClicked(val imageData: ImageData) : GalleryUiEvent()
    data class OnFirstImage(val imageData: ImageData) : GalleryUiEvent()
    data object OnBackClicked : GalleryUiEvent()
    data class OnUpdateZoomData(val cropData: CropData) : GalleryUiEvent()
}