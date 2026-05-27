package app.loococo.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.loococo.domain.model.Diary
import app.loococo.domain.usecase.DiaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

/**
 * 홈 화면 ViewModel — 순수 Flow MVI (Orbit 제거, Phase 5).
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: DiaryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeUiEffect>(
        replay = 0,
        extraBufferCapacity = 1,
    )
    val effect: SharedFlow<HomeUiEffect> = _effect.asSharedFlow()

    private val currentDate =
        LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    init {
        loadDiariesForMonth()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.OnPreviousMonthClicked -> updateMonth(-1)
            HomeUiEvent.OnNextMonthClicked -> updateMonth(1)
            is HomeUiEvent.OnDetailClicked -> navigateToDetail(event.id)
            HomeUiEvent.OnWriteClicked -> navigateToWrite()
        }
    }

    private fun navigateToDetail(id: Long) {
        viewModelScope.launch { _effect.emit(HomeUiEffect.NavigateToDetail(id)) }
    }

    private fun navigateToWrite() {
        viewModelScope.launch { _effect.emit(HomeUiEffect.NavigateToWrite) }
    }

    private fun updateMonth(offset: Long) {
        _state.update { it.copy(currentDate = it.currentDate.plusMonths(offset)) }
        loadDiariesForMonth()
    }

    internal fun loadDiariesForMonth() {
        val currentMonthStart = state.value.currentDate.withDayOfMonth(1)
        state.value.cachedDiaryList[currentMonthStart]?.let { cachedList ->
            updateDiaryState(cachedList)
            return
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fetchAndCacheDiariesForCurrentMonth()
            }
        }
    }

    private suspend fun fetchAndCacheDiariesForCurrentMonth() {
        try {
            useCase.getDiariesForMonth(state.value.currentDate).collect { diaryList ->
                updateDiaryState(diaryList)
                cacheDiariesForMonth(diaryList)
            }
        } catch (_: Exception) {
            clearDiaryState()
        }
    }

    private fun updateDiaryState(diaryList: List<Diary>) {
        val currentStateDate = state.value.currentDate
        val isCurrentMonth = isSameMonthAsToday(currentStateDate)
        val todayDiaryExists = diaryList.any { it.date == currentDate && isCurrentMonth }

        _state.update {
            it.copy(
                diaryList = diaryList,
                todayDiaryState = when {
                    isCurrentMonth && todayDiaryExists -> TodayDiaryState.Completed
                    isCurrentMonth -> TodayDiaryState.Incomplete
                    else -> TodayDiaryState.Hide
                }
            )
        }
    }

    private fun cacheDiariesForMonth(diaryList: List<Diary>) {
        val currentMonthStart = state.value.currentDate.withDayOfMonth(1)
        _state.update {
            it.copy(
                cachedDiaryList = it.cachedDiaryList + (currentMonthStart to diaryList)
            )
        }
    }

    private fun clearDiaryState() {
        _state.update { it.copy(diaryList = emptyList(), todayDiaryState = TodayDiaryState.Hide) }
    }

    private fun isSameMonthAsToday(date: LocalDate): Boolean =
        date.year == LocalDate.now().year && date.month == LocalDate.now().month
}

sealed class TodayDiaryState {
    data object Incomplete : TodayDiaryState()
    data object Completed : TodayDiaryState()
    data object Hide : TodayDiaryState()
}
