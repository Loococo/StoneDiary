package app.loococo.presentation.screen.write.content

import app.loococo.presentation.screen.write.emotion.EmotionEnum
import java.time.LocalDate

data class ContentState(
    val id: Long = 0L,
    val emotion: EmotionEnum = EmotionEnum.HAPPY,
    val currentDate: LocalDate = LocalDate.now(),
    val title: String = "",
    val content: String = "",
    val imageList: MutableList<String> = mutableListOf(),
    val selectedImage: String = "",
    val isLoading: Boolean = false
)

sealed class ContentSideEffect {
    data object NavigateToHome : ContentSideEffect()
    data object NavigateToGallery : ContentSideEffect()
    data object NavigateUp : ContentSideEffect()
    data object DeleteImageDialog : ContentSideEffect()
    data class ShowToast(val res: Int) : ContentSideEffect()
}

sealed class ContentEvent {
    data class OnEmotionUpdated(val emotion: String) : ContentEvent()
    data class OnDiaryIdUpdated(val id: Long) : ContentEvent()
    data class OnTitleUpdated(val title: String) : ContentEvent()
    data class OnContentUpdated(val content: String) : ContentEvent()
    data class OnImageAdded(val image: String) : ContentEvent()
    data class OnDeleteImageClicked(val image: String) : ContentEvent()
    data object OnConfirmDeleteImage : ContentEvent()
    data object OnSaveClicked : ContentEvent()
    data object OnAddImageClicked : ContentEvent()
    data object OnBackClicked : ContentEvent()
}