package app.loococo.data.repository

import app.loococo.data.local.dao.DiaryDao
import app.loococo.data.local.model.DiaryEntity
import app.loococo.data.local.model.toDiary
import app.loococo.domain.model.Diary
import app.loococo.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(private val dao: DiaryDao) : DiaryRepository {
    override fun getDiariesForMonth(startEpochMilli: Long, endEpochMilli: Long): Flow<List<Diary>> {
        return dao.getDiariesForMonth(startEpochMilli, endEpochMilli).map { list ->
            list.map(DiaryEntity::toDiary)
        }
    }
}