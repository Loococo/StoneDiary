package app.loococo.presentation.screen.gallery

import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.loococo.domain.model.image.CropData
import app.loococo.domain.model.image.CropSize
import app.loococo.domain.model.image.ImageData
import app.loococo.presentation.R
import app.loococo.presentation.component.CircularProgressBar
import app.loococo.presentation.component.StoneDiaryAsyncImage
import app.loococo.presentation.component.StoneDiaryNavigationButton
import app.loococo.presentation.theme.White
import app.loococo.presentation.utils.StoneDiaryIcons
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
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
fun SelectedImage(
    imageData: ImageData,
    calculateImageSize: (imageData: ImageData, boxSize: CropSize) -> CropSize,
    calculateScaleFactor: (imageSize: CropSize, boxSize: CropSize) -> Float,
    onEventSent: (event: GalleryEvent) -> Unit
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
        ImageZoom(imageData, boxSize, calculateImageSize, calculateScaleFactor, onEventSent)
    }
}

@Composable
fun ImageZoom(
    imageData: ImageData,
    boxSize: CropSize,
    calculateImageSize: (imageData: ImageData, boxSize: CropSize) -> CropSize,
    calculateScaleFactor: (imageSize: CropSize, boxSize: CropSize) -> Float,
    onEventSent: (event: GalleryEvent) -> Unit
) {
    val context = LocalContext.current
    val imageLoader = context.imageLoader

    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var imageSize by remember { mutableStateOf(CropSize()) }
    var initialScale by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(imageData.image) {
        imageSize = calculateImageSize(imageData, boxSize)
        initialScale = calculateScaleFactor(imageSize, boxSize)
        scale = initialScale
        offsetX = 0f
        offsetY = 0f

        onEventSent(
            GalleryEvent.OnUpdateZoomData(
                CropData(
                    boxSize,
                    imageSize,
                    scale,
                    offsetX,
                    offsetY
                )
            )
        )
    }

    fun Modifier.detectZoomPanGesture() = pointerInput(Unit) {
        detectTransformGestures { centroid, pan, zoom, _ ->
            val newScale = (scale * zoom).coerceIn(initialScale, 5f)

            val newOffsetX = offsetX + pan.x + (centroid.x - boxSize.width / 2) * (newScale - scale)
            val newOffsetY =
                offsetY + pan.y + (centroid.y - boxSize.height / 2) * (newScale - scale)

            val maxOffsetX = ((imageSize.width * newScale - boxSize.width) / 2).coerceAtLeast(0f)
            val maxOffsetY = ((imageSize.height * newScale - boxSize.height) / 2).coerceAtLeast(0f)

            scale = newScale
            offsetX = newOffsetX.coerceIn(-maxOffsetX, maxOffsetX)
            offsetY = newOffsetY.coerceIn(-maxOffsetY, maxOffsetY)
            onEventSent(
                GalleryEvent.OnUpdateZoomData(
                    CropData(
                        boxSize,
                        imageSize,
                        scale,
                        offsetX,
                        offsetY
                    )
                )
            )
        }
    }

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageData.image)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .build(),
        contentDescription = "Zoomable image",
        imageLoader = imageLoader,
        modifier = Modifier
            .detectZoomPanGesture()
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offsetX,
                translationY = offsetY
            )
    )
    DrawGuidelines()
}

@Composable
fun DrawGuidelines() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val (width, height) = size

        drawGuidelineLines(width, height, isVertical = true)
        drawGuidelineLines(width, height, isVertical = false)
    }
}

fun DrawScope.drawGuidelineLines(width: Float, height: Float, isVertical: Boolean) {
    repeat(2) { i ->
        val position = (i + 1) * (if (isVertical) width else height) / 3
        drawLine(
            color = White,
            start = if (isVertical) Offset(position, 0f) else Offset(0f, position),
            end = if (isVertical) Offset(position, height) else Offset(width, position),
            strokeWidth = 2f
        )
    }
}

@Composable
fun ImageGrid(
    lazyPagingItems: LazyPagingItems<ImageData>,
    onEventSent: (event: GalleryEvent) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(2.dp)
    ) {
        items(lazyPagingItems.itemCount) { index ->
            lazyPagingItems[index]?.let { imageData ->
                ImageItem(imageData, onEventSent)
            }
        }
    }
}

@Composable
fun ImageItem(imageData: ImageData, onEventSent: (event: GalleryEvent) -> Unit) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .aspectRatio(1f)
            .clickable { onEventSent(GalleryEvent.OnImageClicked(imageData)) }
    ) {
        StoneDiaryAsyncImage(imageData.image)
    }
}