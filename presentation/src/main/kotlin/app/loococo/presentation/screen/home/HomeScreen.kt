package app.loococo.presentation.screen.home

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.loococo.domain.model.Diary
import app.loococo.presentation.R
import app.loococo.presentation.component.StoneDiaryBodyText
import app.loococo.presentation.component.StoneDiaryHeadlineText
import app.loococo.presentation.component.StoneDiaryLabelText
import app.loococo.presentation.component.StoneDiaryListItem
import app.loococo.presentation.component.StoneDiaryNavigationButton
import app.loococo.presentation.utils.StoneDiaryIcons

@Composable
internal fun HomeRoute(
    onDetail: () -> Unit,
    onWrite: () -> Unit
) {
    HomeScreen(onDetail, onWrite)
}

@Composable
fun HomeScreen(
    onDetail: () -> Unit,
    onWrite: () -> Unit
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        DiaryHeader(
            currentDate = state.formattedDate,
            navigateToPreviousMonth = { viewModel.handleIntent(HomeIntent.NavigateToPreviousMonth) },
            navigateToNextMonth = { viewModel.handleIntent(HomeIntent.NavigateToNextMonth) }
        )
        DiaryList(
            diaryList = state.diaryList,
            todayDiaryState = state.todayDiaryState,
            onDetail = onDetail,
            onWrite = onWrite
        )
    }
}

@Composable
fun DiaryList(
    diaryList: List<Diary>,
    todayDiaryState: TodayDiaryState,
    onDetail: () -> Unit,
    onWrite: () -> Unit
) {
    LazyColumn {
        item {
            when (todayDiaryState) {
                TodayDiaryState.Completed -> CompletedDiaryEntry(onDetail)
                TodayDiaryState.Incomplete -> IncompleteDiaryEntry(onWrite)
                TodayDiaryState.Hide -> {}
            }
        }
        items(diaryList) { DiaryEntryItem(it) }
    }
}

@Composable
fun DiaryHeader(
    currentDate: String,
    navigateToPreviousMonth: () -> Unit,
    navigateToNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 35.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        StoneDiaryNavigationButton(
            size = 35.dp,
            icon = StoneDiaryIcons.ArrowLeft,
            description = "Previous",
            onClick = navigateToPreviousMonth
        )
        StoneDiaryHeadlineText(
            text = currentDate,
            modifier = Modifier.weight(1f)
        )
        StoneDiaryNavigationButton(
            size = 35.dp,
            icon = StoneDiaryIcons.ArrowRight,
            description = "Next",
            onClick = navigateToNextMonth
        )
    }
}

@Composable
fun IncompleteDiaryEntry(onWrite: () -> Unit) {
    StoneDiaryListItem(modifier = Modifier.clickable { onWrite() }) {
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
fun CompletedDiaryEntry(onDetail: () -> Unit) {
    StoneDiaryListItem(modifier = Modifier.clickable { onDetail() }) {
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
fun DiaryEntryItem(item: Diary) {
    StoneDiaryListItem {
        StoneDiaryLabelText(text = stringResource(R.string.month, item.localDate.monthValue))
        VerticalDivider(
            thickness = 1.dp,
            color = Color.Gray,
            modifier = Modifier.padding(5.dp, 2.dp)
        )
        StoneDiaryBodyText(text = item.title)
        Icon(
            imageVector = StoneDiaryIcons.Favorite,
            contentDescription = "",
            modifier = Modifier.size(20.dp)
        )
    }
}