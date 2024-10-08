package app.loococo.presentation.screen.detail

import app.loococo.domain.model.Diary
import app.loococo.presentation.screen.write.content.ContentEvent

data class DetailState(
    val id: Long = 0L,
    val diary: Diary = Diary()
)

sealed class DetailSideEffect {
    data object NavigateToHome : DetailSideEffect()
    data object NavigateUp : DetailSideEffect()
    data class Toast(val res: Int) : DetailSideEffect()
}

sealed class DetailEvent {
    data class DiaryIdEvent(val id: Long) : DetailEvent()
    data object BackClickEvent : DetailEvent()
}