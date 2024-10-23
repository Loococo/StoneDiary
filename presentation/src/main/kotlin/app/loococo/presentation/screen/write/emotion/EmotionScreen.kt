package app.loococo.presentation.screen.write.emotion

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.loococo.presentation.R
import app.loococo.presentation.component.StoneDiaryNavigationButton
import app.loococo.presentation.component.StoneDiaryTitleText
import app.loococo.presentation.theme.Black
import app.loococo.presentation.utils.StoneDiaryIcons
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun EmotionRoute(navigateToWrite: (String, Long) -> Unit, navigateUp: () -> Unit) {
    EmotionScreen(navigateToWrite, navigateUp)
}

@Composable
fun EmotionScreen(navigateToWrite: (String, Long) -> Unit, navigateUp: () -> Unit) {
    val viewModel: EmotionViewModel = hiltViewModel()
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            EmotionSideEffect.NavigateToWrite -> navigateToWrite(state.emotion, state.id)
            EmotionSideEffect.NavigateUp -> navigateUp()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        EmotionHeader(onEventSent = viewModel::onEventReceived)
        EmotionList(onEventSent = viewModel::onEventReceived)
    }
}

@Composable
fun EmotionHeader(onEventSent: (event: EmotionEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        StoneDiaryNavigationButton(
            size = 35.dp,
            icon = StoneDiaryIcons.ArrowLeft,
            description = "Back",
            onClick = { onEventSent(EmotionEvent.OnBackClicked) }
        )
    }
}

@Composable
fun EmotionList(onEventSent: (event: EmotionEvent) -> Unit) {
    val emotions = EmotionEnum.entries
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        StoneDiaryTitleText(
            text = stringResource(R.string.emotion_title),
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(25.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(emotions) { emotion ->
            EmotionListItem(emotion, onEventSent)
        }
    }
}

@Composable
fun EmotionListItem(
    emotion: EmotionEnum,
    onEventSent: (event: EmotionEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .clickable { onEventSent(EmotionEvent.OnEmotionClicked(emotion.name)) }
            .aspectRatio(1f)
            .border(1.dp, Black, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(painterResource(emotion.resId), contentDescription = "")
    }
}