package app.loococo.presentation.screen.gallery

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.loococo.presentation.component.CircularProgressBar
import app.loococo.presentation.component.StoneDiaryAsyncImage
import app.loococo.presentation.component.StoneDiaryNavigationButton
import app.loococo.presentation.utils.StoneDiaryIcons
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun GalleryRoute(navigateUpToWrite: (String) -> Unit, navigateUp: () -> Unit) {
    GalleryScreen(navigateUpToWrite, navigateUp)
}

@Composable
fun GalleryScreen(navigateUpToWrite: (String) -> Unit, navigateUp: () -> Unit) {
    val viewModel: GalleryViewModel = hiltViewModel()

    val state by viewModel.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            GallerySideEffect.NavigateUp -> navigateUp()
            GallerySideEffect.NavigateToWrite -> navigateUpToWrite(state.image)
        }
    }

    val lazyPagingItems = viewModel.imagePager.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        GalleryHeader(onEventSent = viewModel::onEventReceived)
        SelectedImage(Uri.parse(state.image))
        ImageGrid(
            lazyPagingItems = lazyPagingItems,
            onEventSent = viewModel::onEventReceived
        )
    }


    val isLoading = lazyPagingItems.loadState.refresh is LoadState.Loading ||
            lazyPagingItems.loadState.append is LoadState.Loading
    CircularProgressBar(isLoading)
}

@Composable
fun GalleryHeader(onEventSent: (event: GalleryEvent) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StoneDiaryNavigationButton(
            size = 35.dp,
            icon = StoneDiaryIcons.ArrowLeft,
            description = "Back",
            onClick = { onEventSent(GalleryEvent.OnBackClicked) }
        )

        StoneDiaryNavigationButton(
            size = 35.dp,
            icon = StoneDiaryIcons.Check,
            description = "ok",
            onClick = { onEventSent(GalleryEvent.OnSelectedClicked) }
        )
    }
}

@Composable
fun SelectedImage(uri: Uri) {
    Box(modifier = Modifier.aspectRatio(1f)) {
        StoneDiaryAsyncImage(uri)
    }
}

@Composable
fun ImageGrid(
    lazyPagingItems: LazyPagingItems<String>,
    onEventSent: (event: GalleryEvent) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(2.dp)
    ) {
        items(lazyPagingItems.itemCount) { index ->
            lazyPagingItems[index]?.let { uri ->
                ImageItem(uri, onEventSent)
            }
        }
    }
}

@Composable
fun ImageItem(uri: String, onEventSent: (event: GalleryEvent) -> Unit) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .aspectRatio(1f)
            .clickable { onEventSent(GalleryEvent.OnImageClicked(uri)) }
    ) {
        StoneDiaryAsyncImage(uri)
    }
}