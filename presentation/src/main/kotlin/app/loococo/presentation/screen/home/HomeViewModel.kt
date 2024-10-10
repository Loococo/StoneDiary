package app.loococo.presentation.screen.home

import androidx.lifecycle.ViewModel
import app.loococo.domain.model.Diary
import app.loococo.domain.usecase.DiaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
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

    fun handleIntent(intent: HomeEvent) {
        when (intent) {
            HomeEvent.PreviousMonthClickEvent -> updateMonth(-1)
            HomeEvent.NextMonthClickEvent -> updateMonth(1)
            is HomeEvent.DetailClickEvent -> navigateToDetail(intent.id)
            HomeEvent.WriteClickEvent -> navigateToWrite()
        }
    }

    private fun navigateToDetail(id: Long) = intent {
        postSideEffect(HomeSideEffect.NavigateToDetail(id))
    }

    private fun navigateToWrite() = intent {
        postSideEffect(HomeSideEffect.NavigateToWrite)
    }

    private fun updateMonth(offset: Long) = intent {
        reduce { state.copy(currentDate = state.currentDate.plusMonths(offset)) }
        loadDiariesForMonth()
    }

    private fun loadDiariesForMonth() = intent {
        val currentMonthStart = state.currentDate.withDayOfMonth(1)

        state.cachedDiaryList[currentMonthStart]?.let { cachedList ->
            updateDiaryState(cachedList)
            return@intent
        }

        fetchDiariesForCurrentMonth()
    }

    private suspend fun fetchDiariesForCurrentMonth() = intent {
        try {
            useCase.getDiariesForMonth(state.currentDate).collect { diaryList ->
                updateDiaryState(diaryList)
                reduce {
                    state.copy(
                        cachedDiaryList = state.cachedDiaryList + (state.currentDate.withDayOfMonth(
                            1
                        ) to diaryList)
                    )
                }
            }
        } catch (e: Exception) {
            reduce { state.copy(diaryList = emptyList(), todayDiaryState = TodayDiaryState.Hide) }
        }
    }

    private fun updateDiaryState(diaryList: List<Diary>) = intent {
        val todayDiaryExists = diaryList.any {
            it.date == currentDate && isSameMonthAsToday(state.currentDate)
        }
        reduce {
            state.copy(
                diaryList = diaryList,
                todayDiaryState = when {
                    isSameMonthAsToday(state.currentDate) && todayDiaryExists -> TodayDiaryState.Completed
                    isSameMonthAsToday(state.currentDate) -> TodayDiaryState.Incomplete
                    else -> TodayDiaryState.Hide
                }
            )
        }
    }

    private fun isSameMonthAsToday(date: LocalDate): Boolean =
        date.year == LocalDate.now().year && date.month == LocalDate.now().month
}

sealed class TodayDiaryState {
    data object Incomplete : TodayDiaryState()
    data object Completed : TodayDiaryState()
    data object Hide : TodayDiaryState()
}