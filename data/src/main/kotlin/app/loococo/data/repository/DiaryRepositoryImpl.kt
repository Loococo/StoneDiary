package app.loococo.data.repository

import app.loococo.data.local.dao.DiaryDao
import app.loococo.data.local.model.DiaryEntity
import app.loococo.data.local.model.toDiary
import app.loococo.data.local.model.toDiaryEntity
import app.loococo.domain.model.Diary
import app.loococo.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(private val dao: DiaryDao) : DiaryRepository {
    override suspend fun insert(diary: Diary) {
        dao.insert(diary.toDiaryEntity())
    }

    override fun getDiary(id: Long): Flow<Diary> {
        return dao.getDiary(id).map { it.toDiary() }
    }

    override fun getDiariesForMonth(startEpochMilli: Long, endEpochMilli: Long): Flow<List<Diary>> {
        return dao.getDiariesForMonth(startEpochMilli, endEpochMilli).map { list ->
            list.map(DiaryEntity::toDiary)
        }
    }
}