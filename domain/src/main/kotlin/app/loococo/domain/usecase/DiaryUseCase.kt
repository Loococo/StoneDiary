package app.loococo.domain.usecase

import app.loococo.domain.model.Diary
import app.loococo.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class DiaryUseCase @Inject constructor(private val repository: DiaryRepository) {

    suspend fun insert(currentDate: LocalDate, title: String, content: String, emotion: String) {
        val diary = Diary(
            date = currentDate.toEpochMilli(),
            title = title,
            content = content,
            emotion = emotion
        )
        return repository.insert(diary)
    }

    fun getDiary(id:Long):Flow<Diary> {
        return repository.getDiary(id)
    }

    fun getDiariesForMonth(currentDate: LocalDate): Flow<List<Diary>> {
        val (startOfMonth, endOfMonth) = getStartAndEndOfMonth(currentDate)
        return repository.getDiariesForMonth(startOfMonth, endOfMonth)
    }

    private fun getStartAndEndOfMonth(date: LocalDate): Pair<Long, Long> {
        val startOfMonth = date.withDayOfMonth(1)
        val endOfMonth = date.withDayOfMonth(date.lengthOfMonth())
        return startOfMonth.toEpochMilli() to endOfMonth.toEpochMilli()
    }

    private fun LocalDate.toEpochMilli(): Long =
        this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}