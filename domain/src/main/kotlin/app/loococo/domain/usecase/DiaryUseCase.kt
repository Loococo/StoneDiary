package app.loococo.domain.usecase

import app.loococo.domain.model.Diary
import app.loococo.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiaryUseCase @Inject constructor(private val repository: DiaryRepository) {

    fun getDiariesForMonth(startEpochMilli: Long, endEpochMilli: Long): Flow<List<Diary>>{
        return repository.getDiariesForMonth(startEpochMilli, endEpochMilli)
    }

}