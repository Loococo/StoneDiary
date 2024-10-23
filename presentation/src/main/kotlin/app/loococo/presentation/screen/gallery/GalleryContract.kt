package app.loococo.presentation.screen.gallery

data class GalleryState(
    val image: String = ""
)

sealed class GallerySideEffect {
    data object NavigateUp : GallerySideEffect()
    data object NavigateToWrite : GallerySideEffect()
}

sealed class GalleryEvent {
    data object OnSelectedClicked : GalleryEvent()
    data class OnImageClicked(val image: String) : GalleryEvent()
    data object OnBackClicked : GalleryEvent()
}