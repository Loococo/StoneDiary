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
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.placeholder
import coil3.request.error

/**
 * Coil 3 기반 AsyncImage 래퍼.
 * 호출처에서 ImageLoader 를 직접 주입할 필요 없이 Coil 3 의 singleton ImageLoader 가 사용된다.
 */
@Composable
fun StoneDiaryAsyncImage(
    image: String,
    modifier: Modifier,
    transformationState: TransformationState
) {
    val context = LocalPlatformContext.current

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(image)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .build(),
        contentDescription = "Zoom image",
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
    val context = LocalPlatformContext.current

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(image)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .build(),
        contentDescription = "image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
    )
}

@Composable
fun StoneDiaryAsyncImage(image: String) {
    StoneDiaryAsyncImage(Uri.parse(image))
}
