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
import app.loococo.presentation.component.CircularProgressBar
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
import app.loococo.presentation.utils.checkPermission
import app.loococo.presentation.utils.formattedDateWrite
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
        image = image,
        navigateToHome = navigateToHome,
        navigateToGallery = navigateToGallery,
        navigateUp = navigateUp
    )
}

@Composable
private fun ContentScreen(
    image: String,
    navigateToHome: () -> Unit,
    navigateToGallery: () -> Unit,
    navigateUp: () -> Unit
) {
    val viewModel: ContentViewModel = hiltViewModel()
    val state by viewModel.collectAsState()
    val context = LocalContext.current
    var showDeleteImageDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(image) {
        if (image.isNotBlank()) {
            viewModel.onEventReceived(ContentEvent.OnImageAdded(image))
        }
    }

    viewModel.collectSideEffect { effect ->
        when (effect) {
            ContentSideEffect.NavigateUp -> navigateUp()
            ContentSideEffect.NavigateToHome -> navigateToHome()
            ContentSideEffect.NavigateToGallery -> navigateToGallery()
            ContentSideEffect.DeleteImageDialog -> showDeleteImageDialog = true
            is ContentSideEffect.ShowToast -> {
                Toast.makeText(context, effect.res, Toast.LENGTH_SHORT).show()
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
    CircularProgressBar(state.isLoading)
}

@Composable
private fun ContentHeader(onEventSent: (ContentEvent) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        NavigationButton(
            icon = StoneDiaryIcons.ArrowLeft,
            description = "Back",
            onClick = { onEventSent(ContentEvent.OnBackClicked) }
        )
        NavigationButton(
            icon = StoneDiaryIcons.Check,
            description = "Save",
            onClick = { onEventSent(ContentEvent.OnSaveClicked) }
        )
    }
}

@Composable
private fun NavigationButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    onClick: () -> Unit
) {
    StoneDiaryNavigationButton(
        size = 35.dp,
        icon = icon,
        description = description,
        onClick = onClick
    )
}

@Composable
private fun ContentTitle(
    emotion: EmotionEnum,
    currentDate: String,
    title: String,
    onEventSent: (ContentEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        EmotionImage(emotion)
        TitleContent(
            currentDate = currentDate,
            title = title,
            onTitleChange = { onEventSent(ContentEvent.OnTitleUpdated(it)) }
        )
    }
}

@Composable
private fun EmotionImage(emotion: EmotionEnum) {
    Box(modifier = Modifier.border(1.dp, Black, RoundedCornerShape(10.dp))) {
        Image(
            painter = painterResource(emotion.resId),
            contentDescription = "Emotion Image",
            modifier = Modifier
                .size(80.dp)
                .padding(10.dp)
        )
    }
}

@Composable
private fun TitleContent(
    currentDate: String,
    title: String,
    onTitleChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 2.dp)
    ) {
        StoneDiaryLabelText(currentDate)
        StoneDiaryTitleTextField(
            text = title,
            onValueChange = onTitleChange
        )
    }
}

@Composable
private fun ContentBody(
    context: Context,
    content: String,
    imageList: MutableList<String>,
    onEventSent: (ContentEvent) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {
        StoneDiaryContentTextField(
            text = content,
            onValueChange = { onEventSent(ContentEvent.OnContentUpdated(it)) }
        )
        HeightSpacer(height = 10)
        ContentPhoto(
            context = context,
            imageList = imageList,
            onEventSent = onEventSent
        )
    }
}

@Composable
private fun ContentPhoto(
    context: Context,
    imageList: MutableList<String>,
    onEventSent: (ContentEvent) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        items(imageList.asReversed()) { imageUrl ->
            PhotoItem(imageUrl, onEventSent)
        }
        item {
            PhotoAddItem(context, onEventSent)
        }
    }
}

@Composable
private fun PhotoItem(image: String, onEventSent: (ContentEvent) -> Unit) {
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
        DeleteButton { onEventSent(ContentEvent.OnDeleteImageClicked(image)) }
    }
}

@Composable
private fun DeleteButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .offset(x = 80.dp, y = (-10).dp)
            .size(30.dp)
            .background(Black, shape = CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = StoneDiaryIcons.Close,
            contentDescription = "Delete Image",
            tint = White
        )
    }
}

@Composable
private fun PhotoAddItem(context: Context, onEventSent: (ContentEvent) -> Unit) {
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
                    checkPermission(context, permissionLauncher) {
                        onEventSent(ContentEvent.OnAddImageClicked)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = StoneDiaryIcons.Add,
                contentDescription = "Add Image",
                tint = White
            )
        }
    }
}