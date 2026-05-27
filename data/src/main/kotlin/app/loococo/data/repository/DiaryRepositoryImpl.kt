package app.loococo.data.repository

import app.loococo.data.local.room.dao.DiaryDao
import app.loococo.data.repository.mapper.toDomain
import app.loococo.data.repository.mapper.toEntity
import app.loococo.domain.model.Diary
import app.loococo.domain.repository.IDiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(private val dao: DiaryDao) : IDiaryRepository {
    override suspend fun insert(diary: Diary) {
        dao.insert(diary.toEntity())
    }

    override suspend fun update(id: Long, diary: Diary) {
        dao.update(id, diary.title, diary.content, diary.emotion, diary.imageList)
    }

    override suspend fun getDiary(id: Long): Flow<Diary> {
        return dao.getDiary(id).mapNotNull {
            it?.toDomain()
        }
    }

    override suspend fun getDiariesForMonth(
        startEpochMilli: Long,
        endEpochMilli: Long
    ): Flow<List<Diary>> {
        return dao.getDiariesForMonth(startEpochMilli, endEpochMilli).mapNotNull { list ->
            list.mapNotNull {
                it?.toDomain()
            }
        }
    }

    override suspend fun deleteDiary(id: Long) {
        dao.deleteById(id)
    }
}
