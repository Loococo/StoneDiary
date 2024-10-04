package app.loococo.presentation.screen.home

import androidx.lifecycle.ViewModel
import app.loococo.domain.usecase.DiaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: DiaryUseCase) :
    ContainerHost<HomeState, HomeSideEffect>, ViewModel() {

    override val container = container<HomeState, HomeSideEffect>(HomeState())
    private val currentDate =
        LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    init {
        loadDiariesForMonth()
    }

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.NavigateToPreviousMonth -> navigateToPreviousMonth()
            is HomeIntent.NavigateToNextMonth -> navigateToNextMonth()
        }
    }

    private fun navigateToPreviousMonth() = intent {
        reduce { state.copy(currentDate = state.currentDate.minusMonths(1)) }
        loadDiariesForMonth()
    }

    private fun navigateToNextMonth() = intent {
        reduce { state.copy(currentDate = state.currentDate.plusMonths(1)) }
        loadDiariesForMonth()
    }

    private fun loadDiariesForMonth() = intent {
        val (startOfMonth, endOfMonth) = getStartAndEndOfMonth(state.currentDate)

        val cachedData = state.cachedDiaryList[state.currentDate.withDayOfMonth(1)]
        if (cachedData != null) {
            val todayDiaryExists = cachedData.any { it.date == currentDate }
            reduce { state.copy(diaryList = cachedData, isTodayDiary = todayDiaryExists) }
            return@intent
        }

        try {
            useCase.getDiariesForMonth(startOfMonth, endOfMonth).collect { diaryList ->
                val todayDiaryExists = diaryList.any { it.date == currentDate }
                reduce {
                    state.copy(
                        diaryList = diaryList,
                        cachedDiaryList = state.cachedDiaryList + (state.currentDate.withDayOfMonth(
                            1
                        ) to diaryList),
                        isTodayDiary = todayDiaryExists
                    )
                }
            }
        } catch (e: Exception) {
            reduce { state.copy(diaryList = emptyList(), isTodayDiary = false) }
        }
    }

    private fun getStartAndEndOfMonth(date: LocalDate): Pair<Long, Long> {
        val startOfMonth = date.withDayOfMonth(1)
        val endOfMonth = date.withDayOfMonth(date.lengthOfMonth())

        val startEpochMilli =
            startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endEpochMilli =
            endOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        return Pair(startEpochMilli, endEpochMilli)
    }
}