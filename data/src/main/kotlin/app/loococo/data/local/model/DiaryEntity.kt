package app.loococo.data.local.model

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.loococo.domain.model.Diary
import kotlin.concurrent.thread

@Entity(tableName = "diary")
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val date: Long,
    val title: String,
    val content: String,
    val emotion: String,
    val imageList: List<String>
)

fun DiaryEntity.toDiary(): Diary {
    return Diary(
        id = this.id,
        date = this.date,
        title = this.title,
        content = this.content,
        emotion = this.emotion,
        imageList = this.imageList
    )
}

fun Diary.toDiaryEntity(): DiaryEntity {
    return DiaryEntity(
        date = this.date,
        title = this.title,
        content = this.content,
        emotion = this.emotion,
        imageList = this.imageList
    )
}