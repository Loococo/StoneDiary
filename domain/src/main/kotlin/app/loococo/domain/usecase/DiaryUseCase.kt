package app.loococo.domain.usecase

import app.loococo.domain.model.Diary
import app.loococo.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class DiaryUseCase @Inject constructor(private val repository: DiaryRepository) {

    suspend fun insertOrUpdate(
        id: Long,
        currentDate: LocalDate,
        title: String,
        content: String,
        emotion: String,
        imageList: List<String>
    ) {
        val diary = Diary(
            date = currentDate.toEpochMilli(),
            title = title,
            content = content,
            emotion = emotion,
            imageList = imageList
        )
        if (id == 0L) {
            repository.insert(diary)
        } else {
            repository.update(id, diary)
        }
    }

    suspend fun getDiary(id: Long): Flow<Diary> {
        return repository.getDiary(id)
    }

    suspend fun getDiariesForMonth(currentDate: LocalDate): Flow<List<Diary>> {
        val (startOfMonth, endOfMonth) = getStartAndEndOfMonth(currentDate)
        return repository.getDiariesForMonth(startOfMonth, endOfMonth)
    }

    suspend fun deleteDiary(id: Long) {
        repository.deleteDiary(id)
    }

    private fun getStartAndEndOfMonth(date: LocalDate): Pair<Long, Long> {
        val startOfMonth = date.withDayOfMonth(1)
        val endOfMonth = date.withDayOfMonth(date.lengthOfMonth())
        return startOfMonth.toEpochMilli() to endOfMonth.toEpochMilli()
    }

    private fun LocalDate.toEpochMilli(): Long =
        this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}