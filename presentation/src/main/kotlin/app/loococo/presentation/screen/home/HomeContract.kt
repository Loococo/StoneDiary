package app.loococo.presentation.screen.home

import app.loococo.domain.model.Diary
import java.time.LocalDate

data class HomeState(
    val isLoading: Boolean = false,
    val currentDate: LocalDate = LocalDate.now(),
    val diaryList: List<Diary> = emptyList(),
    val cachedDiaryList: Map<LocalDate, List<Diary>> = emptyMap(),
    val todayDiaryState: TodayDiaryState = TodayDiaryState.Hide
)

sealed class HomeSideEffect {
    data class NavigateToDetail(val id: Long) : HomeSideEffect()
    data object NavigateToWrite : HomeSideEffect()
}

sealed class HomeEvent {
    data object OnPreviousMonthClicked : HomeEvent()
    data object OnNextMonthClicked : HomeEvent()
    data class OnDetailClicked(val id: Long) : HomeEvent()
    data object OnWriteClicked : HomeEvent()
}