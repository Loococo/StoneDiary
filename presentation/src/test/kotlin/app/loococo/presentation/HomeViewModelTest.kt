package app.loococo.presentation

import app.loococo.domain.model.Diary
import app.loococo.domain.usecase.DiaryUseCase
import app.loococo.presentation.screen.home.HomeEvent
import app.loococo.presentation.screen.home.HomeSideEffect
import app.loococo.presentation.screen.home.HomeState
import app.loococo.presentation.screen.home.HomeViewModel
import app.loococo.presentation.screen.write.emotion.EmotionEnum
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.orbitmvi.orbit.test.test
import java.time.LocalDate
import java.time.ZoneId

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private lateinit var useCase: DiaryUseCase
    private lateinit var testDiaries: List<Diary>
    private val testPreDate = LocalDate.parse("2124-09-25")
    private val testDate = LocalDate.parse("2124-10-25")
    private val testNextDate = LocalDate.parse("2124-11-25")
    private val testTime = testDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    @Before
    fun setup() {
        testDiaries = listOf(
            Diary(
                id = 1L,
                title = "테스트 일기 제목",
                content = "테스트 일기 내용",
                emotion = EmotionEnum.HAPPY.name,
                date = testTime,
                imageList = emptyList()
            ),
            Diary(
                id = 2L,
                title = "테스트 일기 제목",
                content = "테스트 일기 내용",
                emotion = EmotionEnum.HAPPY.name,
                date = testTime,
                imageList = emptyList()
            )
        )
        useCase = mock(DiaryUseCase::class.java)
        viewModel = HomeViewModel(mock(DiaryUseCase::class.java))
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `이전달 이동 테스트`() = runTest {
        viewModel.test(this, HomeState(currentDate = testDate)) {
            expectInitialState()
            containerHost.onEventReceived(HomeEvent.OnPreviousMonthClicked)
            expectState(HomeState(currentDate = testPreDate))
        }
    }

    @Test
    fun `다음달_이동_테스트`() = runTest {
        viewModel.test(this, HomeState(currentDate = testDate)) {
            expectInitialState()
            containerHost.onEventReceived(HomeEvent.OnNextMonthClicked)
            expectState(HomeState(currentDate = testNextDate))
        }
    }

    @Test
    fun `디테일_화면_이동_테스트`() = runTest {
        viewModel.test(this) {
            expectInitialState()
            containerHost.onEventReceived(HomeEvent.OnDetailClicked(1L))
            expectSideEffect(HomeSideEffect.NavigateToDetail(1L))
        }
    }

    @Test
    fun `작성_화면_이동_테스트`() = runTest {
        viewModel.test(this) {
            expectInitialState()
            containerHost.onEventReceived(HomeEvent.OnWriteClicked)
            expectSideEffect(HomeSideEffect.NavigateToWrite)
        }
    }
}