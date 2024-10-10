package app.loococo.presentation.screen.write.content

import app.loococo.presentation.screen.write.emotion.EmotionEnum
import java.time.LocalDate

data class ContentState(
    val emotion: EmotionEnum = EmotionEnum.HAPPY,
    val currentDate: LocalDate = LocalDate.now(),
    val title: String = "",
    val content: String = "",
)

sealed class ContentSideEffect {
    data object NavigateToHome : ContentSideEffect()
    data object NavigateUp : ContentSideEffect()
    data class Toast(val res: Int) : ContentSideEffect()
}

sealed class ContentEvent {
    data class EmotionEvent(val emotion: String) : ContentEvent()
    data class TitleChangedEvent(val title: String) : ContentEvent()
    data class ContentChangedEvent(val content: String) : ContentEvent()
    data object SaveClickEvent : ContentEvent()
    data object BackClickEvent : ContentEvent()
}