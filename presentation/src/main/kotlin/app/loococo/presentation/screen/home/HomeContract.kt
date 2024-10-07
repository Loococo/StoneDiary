package app.loococo.presentation.screen.home

import app.loococo.domain.model.Diary
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class HomeState(
    val isLoading: Boolean = false,
    val currentDate: LocalDate = LocalDate.now(),
    val diaryList: List<Diary> = emptyList(),
    val cachedDiaryList: Map<LocalDate, List<Diary>> = emptyMap(),
    val todayDiaryState: TodayDiaryState = TodayDiaryState.Hide
) {
    val formattedDate: String
        get() = currentDate.format(DateTimeFormatter.ofPattern("yyyy.MM"))
}

sealed class HomeSideEffect {

}

sealed class HomeIntent {
    data object NavigateToPreviousMonth : HomeIntent()
    data object NavigateToNextMonth : HomeIntent()
}

sealed class TodayDiaryState {
    data object Incomplete : TodayDiaryState()
    data object Completed : TodayDiaryState()
    data object Hide : TodayDiaryState()
}