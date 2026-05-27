package app.loococo.presentation.screen.detail

import app.loococo.domain.model.Diary

data class DetailUiState(
    val id: Long = 0L,
    val diary: Diary = Diary(),
    val isCurrentDiary: Boolean = false
)

sealed class DetailUiEffect {
    data object NavigateToHome : DetailUiEffect()
    data object NavigateToWrite : DetailUiEffect()
    data object NavigateUp : DetailUiEffect()
    data object MoreDialog : DetailUiEffect()
    data class ShowToast(val res: Int) : DetailUiEffect()
}

sealed class DetailUiEvent {
    data class OnDiaryIdUpdated(val id: Long) : DetailUiEvent()
    data object OnBackClicked : DetailUiEvent()
    data object OnMoreDialogClicked : DetailUiEvent()
    data object OnModifyClicked : DetailUiEvent()
    data object OnDeletedClicked : DetailUiEvent()
}