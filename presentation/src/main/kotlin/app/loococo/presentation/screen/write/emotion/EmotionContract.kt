package app.loococo.presentation.screen.write.emotion

data class EmotionState(
    val emotion: String = ""
)
sealed class EmotionSideEffect {
    data object NavigateToWrite : EmotionSideEffect()
    data object NavigateUp : EmotionSideEffect()
}

sealed class EmotionEvent {
    data class OnEmotionClicked(val emotion: String) : EmotionEvent()
    data object OnBackClicked : EmotionEvent()
}