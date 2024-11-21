package app.loococo.presentation.screen.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.loococo.domain.model.image.CropSize
import app.loococo.domain.model.image.ImageData
import app.loococo.presentation.component.CircularProgressBar
import app.loococo.presentation.component.DrawGuidelines
import app.loococo.presentation.component.StoneDiaryAsyncImage
import app.loococo.presentation.component.StoneDiaryNavigationButton
import app.loococo.presentation.screen.gallery.helper.ImageZoomHelper
import app.loococo.presentation.utils.StoneDiaryIcons
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun GalleryRoute(
    navigateUpToWrite: (String) -> Unit,
    navigateUp: () -> Unit
) {
    GalleryScreen(navigateUpToWrite, navigateUp)
}

@Composable
private fun GalleryScreen(
    navigateUpToWrite: (String) -> Unit,
    navigateUp: () -> Unit
) {
    val viewModel: GalleryViewModel = hiltViewModel()
    val state by viewModel.collectAsState()
    val lazyPagingItems = viewModel.imagePager.collectAsLazyPagingItems()

    viewModel.collectSideEffect {
        when (it) {
            GallerySideEffect.NavigateUp -> navigateUp()
            is GallerySideEffect.NavigateToWrite -> navigateUpToWrite(it.image)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        GalleryHeader(onEventSent = viewModel::onEventReceived)
        SelectedImage(
            imageData = state.imageData,
            calculateImageSize = viewModel::calculateImageSize,
            calculateScaleFactor = viewModel::calculateScaleFactor,
            onEventSent = viewModel::onEventReceived
        )
        ImageGrid(
            lazyPagingItems = lazyPagingItems,
            onEventSent = viewModel::onEventReceived
        )
    }

    val isLoading = lazyPagingItems.loadState.refresh is LoadState.Loading ||
            lazyPagingItems.loadState.append is LoadState.Loading

    CircularProgressBar(isLoading || state.isLoading)
}

@Composable
private fun GalleryHeader(onEventSent: (GalleryEvent) -> Unit) {
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
private fun SelectedImage(
    imageData: ImageData,
    calculateImageSize: (ImageData, CropSize) -> CropSize,
    calculateScaleFactor: (CropSize, CropSize) -> Float,
    onEventSent: (GalleryEvent) -> Unit
) {
    var boxSize by remember { mutableStateOf(CropSize()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clipToBounds()
            .onGloballyPositioned { layoutCoordinates ->
                boxSize = CropSize(
                    layoutCoordinates.size.width,
                    layoutCoordinates.size.height
                )
            },
        contentAlignment = Alignment.Center
    ) {
        ImageZoom(
            imageData = imageData,
            boxSize = boxSize,
            calculateImageSize = calculateImageSize,
            calculateScaleFactor = calculateScaleFactor,
            onEventSent = onEventSent
        )
    }
}

@Composable
private fun ImageZoom(
    imageData: ImageData,
    boxSize: CropSize,
    calculateImageSize: (ImageData, CropSize) -> CropSize,
    calculateScaleFactor: (CropSize, CropSize) -> Float,
    onEventSent: (GalleryEvent) -> Unit
) {
    val zoomHelper = remember {
        ImageZoomHelper { cropData ->
            onEventSent(GalleryEvent.OnUpdateZoomData(cropData))
        }
    }

    LaunchedEffect(imageData.image) {
        zoomHelper.initializeZoom(
            imageData,
            boxSize,
            calculateImageSize,
            calculateScaleFactor
        )
    }

    ZoomableImage(
        imageData = imageData,
        boxSize = boxSize,
        zoomHelper = zoomHelper
    )
    DrawGuidelines()
}

@Composable
private fun ZoomableImage(
    imageData: ImageData,
    boxSize: CropSize,
    zoomHelper: ImageZoomHelper
) {
    fun Modifier.detectZoomPanGesture() = pointerInput(Unit) {
        detectTransformGestures { centroid, pan, zoom, _ ->
            zoomHelper.handleZoomPan(
                zoom,
                pan.x,
                pan.y,
                centroid.x,
                centroid.y,
                boxSize
            )
        }
    }

    val transformationState = zoomHelper.getTransformationState()

    StoneDiaryAsyncImage(
        image = imageData.image,
        modifier = Modifier.detectZoomPanGesture(),
        transformationState = transformationState
    )
}

@Composable
private fun ImageGrid(
    lazyPagingItems: LazyPagingItems<ImageData>,
    onEventSent: (GalleryEvent) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(2.dp)
    ) {
        items(lazyPagingItems.itemCount) { index ->
            lazyPagingItems[index]?.let { imageData ->
                ImageItem(
                    imageData = imageData,
                    onEventSent = onEventSent
                )
            }
        }
    }
}

@Composable
private fun ImageItem(
    imageData: ImageData,
    onEventSent: (GalleryEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .aspectRatio(1f)
            .clickable { onEventSent(GalleryEvent.OnImageClicked(imageData)) }
    ) {
        StoneDiaryAsyncImage(imageData.image)
    }
}