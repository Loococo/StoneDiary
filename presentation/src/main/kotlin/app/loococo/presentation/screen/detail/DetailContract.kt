package app.loococo.presentation.screen.detail

import app.loococo.domain.model.Diary

data class DetailState(
    val id: Long = 0L,
    val diary: Diary = Diary(),
    val isCurrentDiary: Boolean = false
)

sealed class DetailSideEffect {
    data object NavigateToHome : DetailSideEffect()
    data object NavigateToWrite : DetailSideEffect()
    data object NavigateUp : DetailSideEffect()
    data object MoreDialog : DetailSideEffect()
    data class ShowToast(val res: Int) : DetailSideEffect()
}

sealed class DetailEvent {
    data class OnDiaryIdUpdated(val id: Long) : DetailEvent()
    data object OnBackClicked : DetailEvent()
    data object OnMoreDialogClicked : DetailEvent()
    data object OnModifyClicked : DetailEvent()
    data object OnDeletedClicked : DetailEvent()
}