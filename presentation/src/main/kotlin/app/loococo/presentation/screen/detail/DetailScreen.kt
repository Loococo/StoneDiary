package app.loococo.presentation.screen.detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.loococo.domain.model.Diary
import app.loococo.presentation.component.HeightSpacer
import app.loococo.presentation.component.StoneDiaryAsyncImage
import app.loococo.presentation.component.StoneDiaryLabelText
import app.loococo.presentation.component.StoneDiaryNavigationButton
import app.loococo.presentation.component.StoneDiaryTitleText
import app.loococo.presentation.screen.write.emotion.formatEmotionEnum
import app.loococo.presentation.theme.Black
import app.loococo.presentation.utils.StoneDiaryIcons
import app.loococo.presentation.utils.formattedDateWrite
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun DetailRoute(navigateToHome: () -> Unit, navigateUp: () -> Unit) {
    DetailScreen(navigateToHome, navigateUp)
}

@Composable
fun DetailScreen(navigateToHome: () -> Unit, navigateUp: () -> Unit) {
    val viewModel: DetailViewModel = hiltViewModel()

    val state by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            DetailSideEffect.NavigateToHome -> navigateToHome()
            DetailSideEffect.NavigateUp -> navigateUp()
            is DetailSideEffect.ShowToast -> {
                Toast.makeText(context, it.res, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DetailHeader(onEventSent = viewModel::onEventReceived)
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
            onClick = { onEventSent(DetailEvent.OnBackClicked) }
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
            .fillMaxSize()
            .padding(20.dp)
    ) {
        StoneDiaryTitleText(diary.content)
        HeightSpacer(height = 10)

        // DetailPhoto를 LazyColumn의 항목으로 이동
        DetailPhoto(diary.imageList)
    }
}

@Composable
fun DetailPhoto(imageList: List<String>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        items(imageList) { image ->
            DetailPhotoItem(image)
        }
    }
}


@Composable
fun DetailPhotoItem(image: String) {
    Box(modifier = Modifier.aspectRatio(1f)) {
        StoneDiaryAsyncImage(image)
    }
}
