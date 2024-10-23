package app.loococo.presentation.screen.write.content

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.loococo.presentation.component.HeightSpacer
import app.loococo.presentation.component.StoneDiaryAsyncImage
import app.loococo.presentation.component.StoneDiaryContentTextField
import app.loococo.presentation.component.StoneDiaryLabelText
import app.loococo.presentation.component.StoneDiaryNavigationButton
import app.loococo.presentation.component.StoneDiaryTitleTextField
import app.loococo.presentation.screen.write.emotion.EmotionEnum
import app.loococo.presentation.theme.Black
import app.loococo.presentation.theme.Gray
import app.loococo.presentation.theme.White
import app.loococo.presentation.utils.StoneDiaryIcons
import app.loococo.presentation.utils.formattedDateWrite
import app.loococo.presentation.utils.handleOnClick
import app.loococo.presentation.utils.handlePermissionResult
import app.loococo.presentation.utils.rememberPermissionLauncher
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun ContentRoute(
    image: String,
    navigateToHome: () -> Unit,
    navigateToGallery: () -> Unit,
    navigateUp: () -> Unit
) {
    ContentScreen(
        image,
        navigateToHome,
        navigateToGallery,
        navigateUp
    )
}

@Composable
fun ContentScreen(
    image: String,
    navigateToHome: () -> Unit,
    navigateToGallery: () -> Unit,
    navigateUp: () -> Unit
) {
    val viewModel: ContentViewModel = hiltViewModel()

    LaunchedEffect(image) {
        viewModel.onEventReceived(ContentEvent.OnImageAdded(image))
    }

    val state by viewModel.collectAsState()
    val context = LocalContext.current

    var showDeleteImageDialog by rememberSaveable { mutableStateOf(false) }

    viewModel.collectSideEffect {
        when (it) {
            ContentSideEffect.NavigateUp -> navigateUp()
            ContentSideEffect.NavigateToHome -> navigateToHome()
            ContentSideEffect.NavigateToGallery -> navigateToGallery()
            ContentSideEffect.DeleteImageDialog -> {
                showDeleteImageDialog = true
            }

            is ContentSideEffect.ShowToast -> {
                Toast.makeText(context, it.res, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ContentHeader(onEventSent = viewModel::onEventReceived)
        ContentTitle(
            emotion = state.emotion,
            currentDate = state.currentDate.formattedDateWrite(),
            title = state.title,
            onEventSent = viewModel::onEventReceived
        )
        ContentBody(
            context = context,
            content = state.content,
            imageList = state.imageList,
            onEventSent = viewModel::onEventReceived
        )
    }

    DeleteImageDialog(
        visible = showDeleteImageDialog,
        onDismissRequest = {
            showDeleteImageDialog = false
        },
        onImageDeleted = {
            viewModel.onEventReceived(ContentEvent.OnConfirmDeleteImage)
        }
    )
}

@Composable
fun ContentHeader(onEventSent: (event: ContentEvent) -> Unit) {
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
            onClick = { onEventSent(ContentEvent.OnBackClicked) }
        )

        StoneDiaryNavigationButton(
            size = 35.dp,
            icon = StoneDiaryIcons.Check,
            description = "ok",
            onClick = { onEventSent(ContentEvent.OnSaveClicked) }
        )
    }
}

@Composable
fun ContentTitle(
    emotion: EmotionEnum,
    currentDate: String,
    title: String,
    onEventSent: (event: ContentEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .border(1.dp, Black, RoundedCornerShape(10.dp))
        ) {
            Image(
                painter = painterResource(emotion.resId),
                contentDescription = "",
                modifier = Modifier
                    .size(80.dp)
                    .padding(10.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 2.dp)
        ) {
            StoneDiaryLabelText(currentDate)
            StoneDiaryTitleTextField(
                text = title,
                onValueChange = { onEventSent(ContentEvent.OnTitleUpdated(it)) }
            )
        }
    }
}

@Composable
fun ContentBody(
    context: Context,
    content: String,
    imageList: MutableList<String>,
    onEventSent: (event: ContentEvent) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            StoneDiaryContentTextField(
                text = content,
                onValueChange = { onEventSent(ContentEvent.OnContentUpdated(it)) }
            )
        }
        HeightSpacer(height = 10)
        ContentPhoto(
            context = context,
            imageList = imageList,
            onEventSent = onEventSent
        )
    }
}

@Composable
fun ContentPhoto(
    context: Context,
    imageList: MutableList<String>,
    onEventSent: (event: ContentEvent) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        item {
            PhotoAddItem(context, onEventSent)
        }
        items(imageList.toList()) {
            PhotoItem(it, onEventSent)
        }
    }
}

@Composable
fun PhotoItem(image: String, onEventSent: (event: ContentEvent) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 10.dp, 10.dp, 0.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            StoneDiaryAsyncImage(image)
        }
        Box(
            modifier = Modifier
                .offset(x = 80.dp, y = (-10).dp)
                .size(30.dp)
                .background(Black, shape = CircleShape)
                .clickable { onEventSent(ContentEvent.OnDeleteImageClicked(image)) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = StoneDiaryIcons.Close,
                contentDescription = null,
                tint = White
            )
        }
    }
}

@Composable
fun PhotoAddItem(context: Context, onEventSent: (event: ContentEvent) -> Unit) {
    val permissionLauncher = rememberPermissionLauncher { permissions ->
        handlePermissionResult(permissions) {
            onEventSent(ContentEvent.OnAddImageClicked)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 10.dp, 5.dp, 0.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Gray, RoundedCornerShape(8.dp))
                .clickable {
                    handleOnClick(context, permissionLauncher) {
                        onEventSent(ContentEvent.OnAddImageClicked)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = StoneDiaryIcons.Add,
                contentDescription = null,
                tint = White
            )
        }
    }
}
