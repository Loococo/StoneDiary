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

    @Query("SELECT * FROM diary WHERE id = :id")
    fun getDiary(id: Long): Flow<DiaryEntity?>

    @Query("SELECT * FROM diary WHERE date >= :startEpochMilli AND date <= :endEpochMilli ORDER BY date DESC")
    fun getDiariesForMonth(startEpochMilli: Long, endEpochMilli: Long): Flow<List<DiaryEntity?>>

    @Query("DELETE FROM diary WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE diary SET title = :title, content = :content, emotion = :emotion, imageList = :imageList WHERE id = :id")
    suspend fun update(
        id: Long,
        title: String,
        content: String,
        emotion: String,
        imageList: List<String>
    )
}