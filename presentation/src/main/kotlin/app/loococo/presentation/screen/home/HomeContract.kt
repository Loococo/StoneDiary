package app.loococo.presentation.screen.home

import app.loococo.domain.model.Diary
import java.time.LocalDate

data class HomeUiState(
    val isLoading: Boolean = false,
    val currentDate: LocalDate = LocalDate.now(),
    val diaryList: List<Diary> = emptyList(),
    val cachedDiaryList: Map<LocalDate, List<Diary>> = emptyMap(),
    val todayDiaryState: TodayDiaryState = TodayDiaryState.Hide
)

sealed class HomeUiEffect {
    data class NavigateToDetail(val id: Long) : HomeUiEffect()
    data object NavigateToWrite : HomeUiEffect()
}

sealed class HomeUiEvent {
    data object OnPreviousMonthClicked : HomeUiEvent()
    data object OnNextMonthClicked : HomeUiEvent()
    data class OnDetailClicked(val id: Long) : HomeUiEvent()
    data object OnWriteClicked : HomeUiEvent()
}