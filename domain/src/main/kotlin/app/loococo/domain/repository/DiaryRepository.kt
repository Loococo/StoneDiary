package app.loococo.domain.repository

import app.loococo.domain.model.Diary
import kotlinx.coroutines.flow.Flow


interface DiaryRepository {
    suspend fun insert(diary: Diary)
    fun getDiary(id: Long): Flow<Diary>
    fun getDiariesForMonth(startEpochMilli: Long, endEpochMilli: Long): Flow<List<Diary>>
}