package app.loococo.presentation.screen.gallery

data class GalleryState(
    val image: String = ""
)

sealed class GallerySideEffect {
    data object NavigateUp : GallerySideEffect()
}

sealed class GalleryEvent {
    data object SaveClickEvent : GalleryEvent()
    data class ImageClickEvent(val image: String) : GalleryEvent()
    data object BackClickEvent : GalleryEvent()
}