package app.loococo.domain.repository

import app.loococo.domain.model.Diary
import kotlinx.coroutines.flow.Flow


interface DiaryRepository {
    suspend fun insert(diary: Diary)
    suspend fun update(id: Long, diary: Diary)
    suspend fun getDiary(id: Long): Flow<Diary>
    suspend fun getDiariesForMonth(startEpochMilli: Long, endEpochMilli: Long): Flow<List<Diary>>
    suspend fun deleteDiary(id: Long)
}