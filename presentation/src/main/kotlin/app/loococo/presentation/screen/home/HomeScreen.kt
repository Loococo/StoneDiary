package app.loococo.presentation.screen.home

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.loococo.domain.model.Diary
import app.loococo.presentation.component.StoneDiaryListItem
import app.loococo.presentation.theme.Black
import app.loococo.presentation.utils.StoneDiaryIcons

@Composable
internal fun HomeRoute() {
    HomeScreen()
}

@Composable
fun HomeScreen() {
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
            isTodayDiary = state.isTodayDiary
        )
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
        DiaryNavigationButton(
            size = 35.dp,
            icon = StoneDiaryIcons.ArrowLeft,
            description = "Previous",
            onClick = navigateToPreviousMonth
        )
        Text(
            modifier = Modifier.weight(1f),
            text = currentDate,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
        )
        DiaryNavigationButton(
            size = 35.dp,
            icon = StoneDiaryIcons.ArrowRight,
            description = "Next",
            onClick = navigateToNextMonth
        )
    }
}

@Composable
fun DiaryNavigationButton(
    size: Dp,
    icon: ImageVector,
    description: String,
    color: Color = Black,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier.size(size),
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            modifier = Modifier.size(size),
            tint = color
        )
    }
}

@Composable
fun DiaryList(
    diaryList: List<Diary>,
    isTodayDiary: Boolean
) {
    LazyColumn {
        item {
            if (isTodayDiary) {
                CompletedDiaryEntry()
            } else {
                IncompleteDiaryEntry()
            }
        }
        items(diaryList) { DiaryEntryItem() }
    }
}

@Composable
fun IncompleteDiaryEntry() {
    StoneDiaryListItem {
        Text(
            text = "오늘의 일기를 기록해주세요.😥",
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
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
        Text(
            text = "오늘의 일기를 기록했습니다.😄",
            fontSize = 16.sp,
            fontWeight = FontWeight.Thin,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = StoneDiaryIcons.Check,
            contentDescription = "",
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun DiaryEntryItem() {
    StoneDiaryListItem {
        Text(
            text = "04일",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        VerticalDivider(
            thickness = 1.dp,
            color = Color.Gray,
            modifier = Modifier.padding(5.dp, 2.dp)
        )
        Text(
            text = "일기 제목~~",
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = StoneDiaryIcons.Favorite,
            contentDescription = "",
            modifier = Modifier.size(20.dp)
        )
    }
}