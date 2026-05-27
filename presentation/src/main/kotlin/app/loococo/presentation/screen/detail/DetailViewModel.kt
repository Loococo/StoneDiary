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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val useCase: DiaryUseCase,
    savedStateHandle: SavedStateHandle
) :
    ContainerHost<DetailUiState, DetailUiEffect>, ViewModel() {
    override val container = container<DetailUiState, DetailUiEffect>(DetailUiState())

    private val id = savedStateHandle.toRoute<AppRoute.Detail>().id

    init {
        onEventReceived(DetailUiEvent.OnDiaryIdUpdated(id))
    }

    fun onEventReceived(event: DetailUiEvent) {
        when (event) {
            is DetailUiEvent.OnDiaryIdUpdated -> onDiaryIdUpdated(event.id)
            DetailUiEvent.OnBackClicked -> onBackClicked()
            DetailUiEvent.OnMoreDialogClicked -> onMoreDialogClicked()
            DetailUiEvent.OnModifyClicked -> onModifyClicked()
            DetailUiEvent.OnDeletedClicked -> onDeletedClicked()
        }
    }

    private fun onModifyClicked() = intent {
        postSideEffect(DetailUiEffect.NavigateToWrite)
    }

    private fun onDeletedClicked() = intent {
        viewModelScope.launch(Dispatchers.Default) {
            useCase.deleteDiary(state.id)
        }
        postSideEffect(DetailUiEffect.NavigateUp)
    }

    private fun onMoreDialogClicked() = intent {
        reduce { state.copy(isCurrentDiary = state.diary.localDate.isEqual(LocalDate.now())) }
        postSideEffect(DetailUiEffect.MoreDialog)
    }

    private fun onBackClicked() = intent {
        postSideEffect(DetailUiEffect.NavigateUp)
    }

    private fun onDiaryIdUpdated(id: Long) = intent {
        if (id == 0L) {
            postSideEffect(DetailUiEffect.ShowToast(R.string.load_diary_data_waring))
            postSideEffect(DetailUiEffect.NavigateToHome)
            return@intent
        }
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getDiary(id).collectLatest {
                reduce { state.copy(id = id, diary = it) }
            }
        }
    }
}