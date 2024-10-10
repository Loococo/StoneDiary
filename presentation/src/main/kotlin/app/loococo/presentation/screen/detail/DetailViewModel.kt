package app.loococo.presentation.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.loococo.domain.usecase.DiaryUseCase
import app.loococo.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val useCase: DiaryUseCase) :
    ContainerHost<DetailState, DetailSideEffect>, ViewModel() {
    override val container = container<DetailState, DetailSideEffect>(DetailState())

    fun handleIntent(intent: DetailEvent) {
        when (intent) {
            is DetailEvent.DiaryIdEvent -> loadDiaryData(intent.id)
            DetailEvent.BackClickEvent -> navigateUp()
        }
    }

    private fun navigateUp() = intent {
        postSideEffect(DetailSideEffect.NavigateUp)
    }

    private fun loadDiaryData(id: Long) = intent {
        if (id == 0L) {
            postSideEffect(DetailSideEffect.Toast(R.string.load_diary_data_waring))
            postSideEffect(DetailSideEffect.NavigateToHome)
            return@intent
        }
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getDiary(id).collectLatest {
                reduce { state.copy(diary = it) }
            }
        }
    }
}