package app.loococo.presentation.screen.write.content

import app.loococo.presentation.screen.write.emotion.EmotionEnum
import java.time.LocalDate

data class ContentUiState(
    val id: Long = 0L,
    val emotion: EmotionEnum = EmotionEnum.HAPPY,
    val currentDate: LocalDate = LocalDate.now(),
    val title: String = "",
    val content: String = "",
    val imageList: MutableList<String> = mutableListOf(),
    val selectedImage: String = "",
    val isLoading: Boolean = false
)

sealed class ContentUiEffect {
    data object NavigateToHome : ContentUiEffect()
    data object NavigateToGallery : ContentUiEffect()
    data object NavigateUp : ContentUiEffect()
    data object DeleteImageDialog : ContentUiEffect()
    data class ShowToast(val res: Int) : ContentUiEffect()
}

sealed class ContentUiEvent {
    data class OnEmotionUpdated(val emotion: String) : ContentUiEvent()
    data class OnDiaryIdUpdated(val id: Long) : ContentUiEvent()
    data class OnTitleUpdated(val title: String) : ContentUiEvent()
    data class OnContentUpdated(val content: String) : ContentUiEvent()
    data class OnImageAdded(val image: String) : ContentUiEvent()
    data class OnDeleteImageClicked(val image: String) : ContentUiEvent()
    data object OnConfirmDeleteImage : ContentUiEvent()
    data object OnSaveClicked : ContentUiEvent()
    data object OnAddImageClicked : ContentUiEvent()
    data object OnBackClicked : ContentUiEvent()
}