package app.loococo.presentation.screen.write.emotion

data class EmotionUiState(
    val emotion: String = "",
    val id: Long = 0L
)

sealed class EmotionUiEffect {
    data object NavigateToWrite : EmotionUiEffect()
    data object NavigateUp : EmotionUiEffect()
}

sealed class EmotionUiEvent {
    data class OnEmotionClicked(val emotion: String) : EmotionUiEvent()
    data class OnDiaryIdUpdated(val id: Long) : EmotionUiEvent()
    data object OnBackClicked : EmotionUiEvent()
}