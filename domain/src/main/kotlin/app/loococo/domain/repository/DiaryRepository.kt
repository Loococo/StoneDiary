package app.loococo.domain.repository

import app.loococo.domain.model.Diary
import kotlinx.coroutines.flow.Flow


interface DiaryRepository {
    fun getDiariesForMonth(startEpochMilli: Long, endEpochMilli: Long): Flow<List<Diary>>
}