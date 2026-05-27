package app.loococo.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.loococo.domain.model.Diary
import app.loococo.domain.usecase.DiaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    ContainerHost<HomeUiState, HomeUiEffect>, ViewModel() {

    override val container = container<HomeUiState, HomeUiEffect>(HomeUiState())

    private val currentDate =
        LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()


    init {
        loadDiariesForMonth()
    }

    fun onEventReceived(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.OnPreviousMonthClicked -> updateMonth(-1)
            HomeUiEvent.OnNextMonthClicked -> updateMonth(1)
            is HomeUiEvent.OnDetailClicked -> navigateToDetail(event.id)
            HomeUiEvent.OnWriteClicked -> navigateToWrite()
        }
    }

    private fun navigateToDetail(id: Long) = intent {
        postSideEffect(HomeUiEffect.NavigateToDetail(id))
    }

    private fun navigateToWrite() = intent {
        postSideEffect(HomeUiEffect.NavigateToWrite)
    }

    private fun updateMonth(offset: Long) = intent {
        reduce { state.copy(currentDate = state.currentDate.plusMonths(offset)) }
        loadDiariesForMonth()
    }


    internal fun loadDiariesForMonth() = intent {
        val currentMonthStart = state.currentDate.withDayOfMonth(1)
        state.cachedDiaryList[currentMonthStart]?.let { cachedList ->
            updateDiaryState(cachedList)
            return@intent
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fetchAndCacheDiariesForCurrentMonth()
            }
        }
    }

    private suspend fun fetchAndCacheDiariesForCurrentMonth() = intent {
        try {
            useCase.getDiariesForMonth(state.currentDate).collect { diaryList ->
                updateDiaryState(diaryList)
                cacheDiariesForMonth(diaryList)
            }
        } catch (e: Exception) {
            clearDiaryState()
        }
    }

    private fun updateDiaryState(diaryList: List<Diary>) = intent {
        val isCurrentMonth = isSameMonthAsToday(state.currentDate)
        val todayDiaryExists = diaryList.any { it.date == currentDate && isCurrentMonth }

        reduce {
            state.copy(
                diaryList = diaryList,
                todayDiaryState = when {
                    isCurrentMonth && todayDiaryExists -> TodayDiaryState.Completed
                    isCurrentMonth -> TodayDiaryState.Incomplete
                    else -> TodayDiaryState.Hide
                }
            )
        }
    }

    private fun cacheDiariesForMonth(diaryList: List<Diary>) = intent {
        val currentMonthStart = state.currentDate.withDayOfMonth(1)
        reduce {
            state.copy(
                cachedDiaryList = state.cachedDiaryList + (currentMonthStart to diaryList)
            )
        }
    }

    private fun clearDiaryState() = intent {
        reduce {
            state.copy(diaryList = emptyList(), todayDiaryState = TodayDiaryState.Hide)
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