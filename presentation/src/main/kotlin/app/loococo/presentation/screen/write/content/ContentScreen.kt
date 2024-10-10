package app.loococo.presentation.screen.write.content

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.loococo.presentation.component.HeightSpacer
import app.loococo.presentation.component.StoneDiaryContentTextField
import app.loococo.presentation.component.StoneDiaryLabelText
import app.loococo.presentation.component.StoneDiaryNavigationButton
import app.loococo.presentation.component.StoneDiaryTitleTextField
import app.loococo.presentation.screen.write.emotion.EmotionEnum
import app.loococo.presentation.theme.Black
import app.loococo.presentation.utils.StoneDiaryIcons
import app.loococo.presentation.utils.formattedDateWrite

@Composable
internal fun ContentRoute(emotion: String, navigateToHome: () -> Unit, navigateUp: () -> Unit) {
    ContentScreen(emotion, navigateToHome, navigateUp)
}

@Composable
fun ContentScreen(emotion: String, navigateToHome: () -> Unit, navigateUp: () -> Unit) {
    val viewModel: ContentViewModel = hiltViewModel()
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffectFlow = viewModel.container.sideEffectFlow
    val context = LocalContext.current

    LaunchedEffect(sideEffectFlow) {
        sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                ContentSideEffect.NavigateUp -> navigateUp()
                ContentSideEffect.NavigateToHome -> navigateToHome()
                is ContentSideEffect.Toast -> {
                    Toast.makeText(context, sideEffect.res, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LaunchedEffect(emotion) {
        viewModel.handleIntent(ContentEvent.EmotionEvent(emotion))
    }

    Column(modifier = Modifier.fillMaxSize()) {

        ContentHeader(onEventSent = viewModel::handleIntent)
        ContentTitle(
            emotion = state.emotion,
            currentDate = state.currentDate.formattedDateWrite(),
            onEventSent = viewModel::handleIntent
        )
        ContentBody(
            onEventSent = viewModel::handleIntent
        )
    }
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
            onClick = { onEventSent(ContentEvent.BackClickEvent) }
        )

        StoneDiaryNavigationButton(
            size = 35.dp,
            icon = StoneDiaryIcons.Check,
            description = "ok",
            onClick = { onEventSent(ContentEvent.SaveClickEvent) }
        )
    }
}

@Composable
fun ContentTitle(
    emotion: EmotionEnum,
    currentDate: String,
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
                onValueChange = { onEventSent(ContentEvent.TitleChangedEvent(it)) }
            )
        }
    }
}

@Composable
fun ContentBody(onEventSent: (event: ContentEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        StoneDiaryContentTextField(
            onValueChange = { onEventSent(ContentEvent.ContentChangedEvent(it)) }
        )
        HeightSpacer(height = 10)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.Gray)
        )
    }
}