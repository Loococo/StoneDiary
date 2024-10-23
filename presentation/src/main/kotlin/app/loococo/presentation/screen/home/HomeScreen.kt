package app.loococo.presentation.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.loococo.domain.model.Diary
import app.loococo.presentation.R
import app.loococo.presentation.component.StoneDiaryBodyText
import app.loococo.presentation.component.StoneDiaryHeadlineText
import app.loococo.presentation.component.StoneDiaryLabelText
import app.loococo.presentation.component.StoneDiaryListItem
import app.loococo.presentation.component.StoneDiaryNavigationButton
import app.loococo.presentation.screen.write.emotion.formatEmotionEnum
import app.loococo.presentation.utils.StoneDiaryIcons
import app.loococo.presentation.utils.formattedHomeDate
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun HomeRoute(
    navigateToDetail: (Long) -> Unit,
    navigateToWrite: () -> Unit
) {
    HomeScreen(navigateToDetail, navigateToWrite)
}

@Composable
fun HomeScreen(
    navigateToDetail: (Long) -> Unit,
    navigateToWrite: () -> Unit
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is HomeSideEffect.NavigateToDetail -> navigateToDetail(it.id)
            HomeSideEffect.NavigateToWrite -> navigateToWrite()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DiaryHeader(
            currentDate = state.currentDate.formattedHomeDate(),
            onEventSent = viewModel::onEventReceived
        )
        DiaryList(
            diaryList = state.diaryList,
            todayDiaryState = state.todayDiaryState,
            onEventSent = viewModel::onEventReceived
        )
    }
}

@Composable
fun DiaryList(
    diaryList: List<Diary>,
    todayDiaryState: TodayDiaryState,
    onEventSent: (event: HomeEvent) -> Unit
) {
    LazyColumn {
        item {
            when (todayDiaryState) {
                TodayDiaryState.Completed -> CompletedDiaryEntry()
                TodayDiaryState.Incomplete -> IncompleteDiaryEntry(onEventSent)
                TodayDiaryState.Hide -> {}
            }
        }
        items(diaryList) { DiaryEntryItem(it, onEventSent) }
    }
}

@Composable
fun DiaryHeader(
    currentDate: String,
    onEventSent: (event: HomeEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        StoneDiaryNavigationButton(
            size = 35.dp,
            icon = StoneDiaryIcons.ArrowLeft,
            description = "Previous",
            onClick = { onEventSent(HomeEvent.OnPreviousMonthClicked) }
        )
        StoneDiaryHeadlineText(
            text = currentDate,
            modifier = Modifier.weight(1f)
        )
        StoneDiaryNavigationButton(
            size = 35.dp,
            icon = StoneDiaryIcons.ArrowRight,
            description = "Next",
            onClick = { onEventSent(HomeEvent.OnNextMonthClicked) }
        )
    }
}

@Composable
fun IncompleteDiaryEntry(onEventSent: (event: HomeEvent) -> Unit) {
    StoneDiaryListItem(
        modifier = Modifier
            .clickable { onEventSent(HomeEvent.OnWriteClicked) }
    ) {
        StoneDiaryBodyText(
            text = stringResource(R.string.incomplete_diary),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
        Icon(
            imageVector = StoneDiaryIcons.ArrowRight,
            contentDescription = "",
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun CompletedDiaryEntry() {
    StoneDiaryListItem {
        StoneDiaryBodyText(
            text = stringResource(R.string.completed_diary),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
        Icon(
            imageVector = StoneDiaryIcons.Check,
            contentDescription = "",
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun DiaryEntryItem(item: Diary, onEventSent: (event: HomeEvent) -> Unit) {
    StoneDiaryListItem(
        modifier = Modifier
            .clickable { onEventSent(HomeEvent.OnDetailClicked(item.id)) }
    ) {
        StoneDiaryLabelText(text = stringResource(R.string.month, item.localDate.dayOfMonth))
        VerticalDivider(
            thickness = 1.dp,
            color = Color.Gray,
            modifier = Modifier.padding(7.dp, 3.dp)
        )
        StoneDiaryBodyText(
            text = item.title,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = painterResource(item.emotion.formatEmotionEnum().resId),
            contentDescription = "",
            modifier = Modifier.size(30.dp)
        )
    }
}