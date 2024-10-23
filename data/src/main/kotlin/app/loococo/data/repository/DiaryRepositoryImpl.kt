package app.loococo.data.repository

import app.loococo.data.local.dao.DiaryDao
import app.loococo.data.local.model.toDiary
import app.loococo.data.local.model.toDiaryEntity
import app.loococo.domain.model.Diary
import app.loococo.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(private val dao: DiaryDao) : DiaryRepository {
    override suspend fun insert(diary: Diary) {
        dao.insert(diary.toDiaryEntity())
    }

    override suspend fun update(id: Long, diary: Diary) {
        dao.update(id, diary.title, diary.content, diary.emotion, diary.imageList)
    }

    override suspend fun getDiary(id: Long): Flow<Diary> {
        return dao.getDiary(id).mapNotNull {
            it?.toDiary()
        }
    }

    override suspend fun getDiariesForMonth(
        startEpochMilli: Long,
        endEpochMilli: Long
    ): Flow<List<Diary>> {
        return dao.getDiariesForMonth(startEpochMilli, endEpochMilli).mapNotNull { list ->
            list.mapNotNull {
                it?.toDiary()
            }
        }
    }

    override suspend fun deleteDiary(id: Long) {
        dao.deleteById(id)
    }
}