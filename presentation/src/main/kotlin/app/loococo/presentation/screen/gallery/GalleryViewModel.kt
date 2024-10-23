package app.loococo.presentation.screen.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import app.loococo.domain.usecase.ImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    useCase: ImageUseCase
) :
    ContainerHost<GalleryState, GallerySideEffect>, ViewModel() {
    override val container = container<GalleryState, GallerySideEffect>(GalleryState())

    val imagePager: Flow<PagingData<String>> = useCase()

    fun handleIntent(intent: GalleryEvent) {
        when (intent) {
            GalleryEvent.BackClickEvent -> navigateUp()
            is GalleryEvent.ImageClickEvent -> updateImage(intent.image)
            GalleryEvent.SaveClickEvent -> navigateUp()
        }
    }

    private fun updateImage(image: String) = intent {
        reduce { state.copy(image = image) }
    }

    private fun navigateUp() = intent {
        postSideEffect(GallerySideEffect.NavigateUp)
    }
}