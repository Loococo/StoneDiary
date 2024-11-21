package app.loococo.presentation.component

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import app.loococo.presentation.R
import app.loococo.presentation.screen.gallery.helper.TransformationState
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest

@Composable
fun StoneDiaryAsyncImage(
    image: String,
    modifier: Modifier,
    transformationState: TransformationState
) {
    val context = LocalContext.current
    val imageLoader = context.imageLoader

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(image)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .build(),
        contentDescription = "Zoom image",
        imageLoader = imageLoader,
        modifier = modifier
            .graphicsLayer(
                scaleX = transformationState.scale,
                scaleY = transformationState.scale,
                translationX = transformationState.offsetX,
                translationY = transformationState.offsetY
            )
    )
}

@Composable
fun StoneDiaryAsyncImage(image: Uri) {
    val context = LocalContext.current
    val imageLoader = context.imageLoader

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(image)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .build(),
        contentDescription = "image",
        imageLoader = imageLoader,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
    )
}

@Composable
fun StoneDiaryAsyncImage(image: String) {
    StoneDiaryAsyncImage(Uri.parse(image))
}

