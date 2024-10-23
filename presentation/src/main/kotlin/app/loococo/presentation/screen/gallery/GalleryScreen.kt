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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.loococo.presentation.component.StoneDiaryAsyncImage
import app.loococo.presentation.component.StoneDiaryNavigationButton
import app.loococo.presentation.utils.StoneDiaryIcons

@Composable
internal fun GalleryRoute(navigateUpToWrite: (String) -> Unit) {
    GalleryScreen(navigateUpToWrite)
}

@Composable
fun GalleryScreen(navigateUpToWrite: (String) -> Unit) {
    val viewModel: GalleryViewModel = hiltViewModel()
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffectFlow = viewModel.container.sideEffectFlow

    LaunchedEffect(sideEffectFlow) {
        sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                GallerySideEffect.NavigateUp -> navigateUpToWrite(state.image)
            }
        }
    }

    val lazyPagingItems = viewModel.imagePager.collectAsLazyPagingItems()

    LaunchedEffect(lazyPagingItems.loadState) {
        if (lazyPagingItems.loadState.refresh is LoadState.NotLoading && lazyPagingItems.itemCount > 0) {
            lazyPagingItems[0]?.let { firstImageUri ->
                viewModel.handleIntent(GalleryEvent.ImageClickEvent(firstImageUri))
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        GalleryHeader(onEventSent = viewModel::handleIntent)
        SelectedImage(Uri.parse(state.image))
        ImageGrid(
            lazyPagingItems = lazyPagingItems,
            onEventSent = viewModel::handleIntent
        )
    }
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
            onClick = { onEventSent(GalleryEvent.BackClickEvent) }
        )

        StoneDiaryNavigationButton(
            size = 35.dp,
            icon = StoneDiaryIcons.Check,
            description = "ok",
            onClick = { onEventSent(GalleryEvent.SaveClickEvent) }
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

        lazyPagingItems.apply {
            when (loadState.append) {
                is LoadState.Loading -> {
                }

                is LoadState.Error -> {
                }

                else -> Unit
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
            .clickable { onEventSent(GalleryEvent.ImageClickEvent(uri)) }
    ) {
        StoneDiaryAsyncImage(uri)
    }
}