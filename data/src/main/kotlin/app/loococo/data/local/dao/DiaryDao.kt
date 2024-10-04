package app.loococo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import app.loococo.data.local.model.DiaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Insert
    suspend fun insert(diaryEntity: DiaryEntity)

    @Query("SELECT * FROM diary WHERE date >= :startEpochMilli AND date <= :endEpochMilli")
    fun getDiariesForMonth(startEpochMilli: Long, endEpochMilli: Long): Flow<List<DiaryEntity>>
}