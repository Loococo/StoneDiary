package app.loococo.data.local.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room 다이어리 Entity.
 * 도메인 모델(Diary)과의 변환은 data/repository/mapper/DiaryMappers.kt 참조.
 */
@Entity(tableName = "diary")
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val date: Long,
    val title: String,
    val content: String,
    val emotion: String,
    val imageList: List<String>
)
