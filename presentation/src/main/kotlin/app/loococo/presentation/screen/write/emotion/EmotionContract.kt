package app.loococo.presentation.screen.write.emotion

data class EmotionState(
    val emotion: String = "",
    val id: Long = 0L
)

sealed class EmotionSideEffect {
    data object NavigateToWrite : EmotionSideEffect()
    data object NavigateUp : EmotionSideEffect()
}

sealed class EmotionEvent {
    data class OnEmotionClicked(val emotion: String) : EmotionEvent()
    data class OnDiaryIdUpdated(val id: Long) : EmotionEvent()
    data object OnBackClicked : EmotionEvent()
}