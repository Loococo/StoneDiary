package app.loococo.presentation.component

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.imageLoader

@Composable
fun StoneDiaryAsyncImage(image: Uri) {
    val imageLoader = LocalContext.current.imageLoader

    AsyncImage(
        model = image,
        contentDescription = "image",
        imageLoader = imageLoader,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun StoneDiaryAsyncImage(image: String) {
    StoneDiaryAsyncImage(Uri.parse(image))
}

