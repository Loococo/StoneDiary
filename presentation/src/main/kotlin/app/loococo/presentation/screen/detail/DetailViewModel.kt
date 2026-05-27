package app.loococo.presentation.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import app.loococo.domain.usecase.DiaryUseCase
import app.loococo.presentation.R
import app.loococo.presentation.screen.AppRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * 다이어리 상세 화면 ViewModel — 순수 Flow MVI (Orbit 제거, Phase 5).
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val useCase: DiaryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetailUiState())
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<DetailUiEffect>(
        replay = 0,
        extraBufferCapacity = 1,
    )
    val effect: SharedFlow<DetailUiEffect> = _effect.asSharedFlow()

    private val id = savedStateHandle.toRoute<AppRoute.Detail>().id

    init {
        onEvent(DetailUiEvent.OnDiaryIdUpdated(id))
    }

    fun onEvent(event: DetailUiEvent) {
        when (event) {
            is DetailUiEvent.OnDiaryIdUpdated -> handleDiaryIdUpdated(event.id)
            DetailUiEvent.OnBackClicked -> emitEffect(DetailUiEffect.NavigateUp)
            DetailUiEvent.OnMoreDialogClicked -> handleMoreDialogClicked()
            DetailUiEvent.OnModifyClicked -> emitEffect(DetailUiEffect.NavigateToWrite)
            DetailUiEvent.OnDeletedClicked -> handleDeletedClicked()
        }
    }

    private fun handleDeletedClicked() {
        viewModelScope.launch(Dispatchers.Default) {
            useCase.deleteDiary(state.value.id)
            _effect.emit(DetailUiEffect.NavigateUp)
        }
    }

    private fun handleMoreDialogClicked() {
        _state.update {
            it.copy(isCurrentDiary = it.diary.localDate.isEqual(LocalDate.now()))
        }
        emitEffect(DetailUiEffect.MoreDialog)
    }

    private fun handleDiaryIdUpdated(id: Long) {
        if (id == 0L) {
            viewModelScope.launch {
                _effect.emit(DetailUiEffect.ShowToast(R.string.load_diary_data_waring))
                _effect.emit(DetailUiEffect.NavigateToHome)
            }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getDiary(id).collectLatest { diary ->
                _state.update { it.copy(id = id, diary = diary) }
            }
        }
    }

    private fun emitEffect(effect: DetailUiEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
