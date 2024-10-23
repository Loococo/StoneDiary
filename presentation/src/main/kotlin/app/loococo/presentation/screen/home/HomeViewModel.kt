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

    fun onEventReceived(event: HomeEvent) {
        when (event) {
            HomeEvent.OnPreviousMonthClicked -> updateMonth(-1)
            HomeEvent.OnNextMonthClicked -> updateMonth(1)
            is HomeEvent.OnDetailClicked -> navigateToDetail(event.id)
            HomeEvent.OnWriteClicked -> navigateToWrite()
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

        // 캐시된 다이어리 목록에서 현재 월의 다이어리를 가져옵니다.
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

                // 캐시에 현재 월의 다이어리 목록 추가
                reduce {
                    state.copy(
                        cachedDiaryList = state.cachedDiaryList + (state.currentDate.withDayOfMonth(
                            1
                        ) to diaryList)
                    )
                }
            }
        } catch (e: Exception) {
            // 에러 발생 시 다이어리 목록 초기화 및 상태 업데이트
            reduce { state.copy(diaryList = emptyList(), todayDiaryState = TodayDiaryState.Hide) }
            // 에러 로그 추가 (선택 사항)
            // Log.e("HomeScreenViewModel", "Error fetching diaries for current month", e)
        }
    }

    private fun updateDiaryState(diaryList: List<Diary>) = intent {
        val todayDiaryExists =
            diaryList.any { it.date == currentDate && isSameMonthAsToday(state.currentDate) }

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