package app.loococo.presentation.screen.detail

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
import app.loococo.domain.model.Diary
import app.loococo.presentation.component.HeightSpacer
import app.loococo.presentation.component.StoneDiaryContentTextField
import app.loococo.presentation.component.StoneDiaryHeadlineText
import app.loococo.presentation.component.StoneDiaryLabelText
import app.loococo.presentation.component.StoneDiaryNavigationButton
import app.loococo.presentation.component.StoneDiaryTitleText
import app.loococo.presentation.component.StoneDiaryTitleTextField
import app.loococo.presentation.screen.write.content.ContentEvent
import app.loococo.presentation.screen.write.emotion.EmotionEnum
import app.loococo.presentation.screen.write.emotion.formatEmotionEnum
import app.loococo.presentation.theme.Black
import app.loococo.presentation.utils.StoneDiaryIcons
import app.loococo.presentation.utils.formattedDateWrite

@Composable
internal fun DetailRoute(id: Long, navigateToHome: () -> Unit, navigateUp: () -> Unit) {
    DetailScreen(id, navigateToHome, navigateUp)
}

@Composable
fun DetailScreen(id: Long, navigateToHome: () -> Unit, navigateUp: () -> Unit) {
    val viewModel: DetailViewModel = hiltViewModel()
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffectFlow = viewModel.container.sideEffectFlow
    val context = LocalContext.current

    LaunchedEffect(sideEffectFlow) {
        sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                DetailSideEffect.NavigateToHome -> navigateToHome()
                DetailSideEffect.NavigateUp -> navigateUp()
                is DetailSideEffect.Toast -> {
                    Toast.makeText(context, sideEffect.res, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    LaunchedEffect(id) {
        viewModel.handleIntent(DetailEvent.DiaryIdEvent(id))
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DetailHeader(onEventSent = viewModel::handleIntent)
        DetailTitle(diary = state.diary)
        DetailBody(diary = state.diary)
    }
}

@Composable
fun DetailHeader(onEventSent: (event: DetailEvent) -> Unit) {
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
            onClick = { onEventSent(DetailEvent.BackClickEvent) }
        )

        StoneDiaryNavigationButton(
            size = 35.dp,
            icon = StoneDiaryIcons.More,
            description = "more",
            onClick = { }
        )
    }
}

@Composable
fun DetailTitle(diary: Diary) {
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
                painter = painterResource(diary.emotion.formatEmotionEnum().resId),
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
            StoneDiaryLabelText(diary.localDate.formattedDateWrite())
            StoneDiaryTitleText(diary.title)
        }
    }
}

@Composable
fun DetailBody(diary: Diary) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        StoneDiaryTitleText(diary.content)
    }
}
