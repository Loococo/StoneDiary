package app.loococo.presentation.screen.gallery

import app.loococo.domain.model.image.CropData
import app.loococo.domain.model.image.ImageData

data class GalleryState(
    val imageData: ImageData = ImageData(),
    val cropData: CropData = CropData(),
    val isLoading: Boolean = false
)

sealed class GallerySideEffect {
    data object NavigateUp : GallerySideEffect()
    data class NavigateToWrite(val image: String) : GallerySideEffect()
}

sealed class GalleryEvent {
    data object OnSelectedClicked : GalleryEvent()
    data class OnImageClicked(val imageData: ImageData) : GalleryEvent()
    data class OnFirstImage(val imageData: ImageData) : GalleryEvent()
    data object OnBackClicked : GalleryEvent()
    data class OnUpdateZoomData(val cropData: CropData) : GalleryEvent()
}